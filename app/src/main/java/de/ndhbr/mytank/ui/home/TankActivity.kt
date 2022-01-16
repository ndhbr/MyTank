package de.ndhbr.mytank.ui.home

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.ndhbr.mytank.databinding.ActivityTankBinding
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import de.ndhbr.mytank.R
import de.ndhbr.mytank.adapters.FishStockListAdapter
import de.ndhbr.mytank.interfaces.TankItemListener
import de.ndhbr.mytank.models.Tank
import de.ndhbr.mytank.models.TankItem
import de.ndhbr.mytank.utilities.InjectorUtils
import de.ndhbr.mytank.viewmodels.TankItemsViewModel
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.size
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.firebase.storage.UploadTask
import de.ndhbr.mytank.data.ImageStorage
import de.ndhbr.mytank.utilities.Constants
import de.ndhbr.mytank.utilities.ImageUtils
import de.ndhbr.mytank.viewmodels.TanksViewModel


class TankActivity : AppCompatActivity(), TankItemListener {

    // DEBUG
    private val TAG = "TANK_ACTIVITY"

    // UI
    private lateinit var binding: ActivityTankBinding

    // Data
    private lateinit var tank: Tank
    private lateinit var tanksViewModel: TanksViewModel
    private lateinit var tankItemsViewModel: TankItemsViewModel
    private lateinit var editTankIntentLauncher: ActivityResultLauncher<Intent>

    // Images
    private val imageStorage = ImageStorage.getInstance()
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private var uploadTask: UploadTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityTankBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent results
        registerTankImageResult()
        registerEditTankResult()

        // Build UI
        initializeUi(intent)
    }

    override fun onDestroy() {
        if (uploadTask != null && uploadTask!!.isInProgress) {
            uploadTask!!.cancel()
        }
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tank_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.btn_edit -> {
            // Send to update tank screen
            val intent = Intent(
                this@TankActivity,
                AddUpdateTankActivity::class.java
            )
            intent.putExtra("tank", tank)
            editTankIntentLauncher.launch(intent)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun initializeUi(intent: Intent) {
        // Activity parameters
        intent.getParcelableExtra<Tank>("tank").also {
            if (it != null) {
                tank = it
                supportActionBar?.title = tank.name
            }
        }

        // Tanks
        val tanksViewModelFactory = InjectorUtils.provideTanksViewModelFactory()
        tanksViewModel = ViewModelProvider(this, tanksViewModelFactory)
            .get(TanksViewModel::class.java)

        // Tank items
        val tankItemsViewModelFactory = InjectorUtils.provideTankItemsViewModelFactory()
        tankItemsViewModel = ViewModelProvider(this, tankItemsViewModelFactory)
            .get(TankItemsViewModel::class.java)

        // Items list
        val recyclerView = binding.rvFishStockList
        val fishStockAdapter = FishStockListAdapter(ArrayList(), this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = fishStockAdapter
        tankItemsViewModel.getTankItems(tank.tankId!!).observe(this,
            { tankItems ->
                if (tankItems.isEmpty()) {
                    binding.llNoTankItemsHint.visibility = View.VISIBLE
                    binding.rvFishStockList.visibility = View.GONE
                } else {
                    binding.llNoTankItemsHint.visibility = View.GONE
                    binding.rvFishStockList.visibility = View.VISIBLE
                }

                // max number of tank items
                if (binding.rvFishStockList.size >= Constants.MAX_DIFFERENT_TANK_ITEMS) {
                    binding.fabAddTankItem.visibility = View.GONE
                } else {
                    binding.fabAddTankItem.visibility = View.VISIBLE
                }

                fishStockAdapter.updateData(tankItems as ArrayList<TankItem>)
            })

        // New tank item
        binding.fabAddTankItem.setOnClickListener {
            val intent = Intent(this, AddUpdateTankItemActivity::class.java)
            intent.putExtra("tank-id", tank.tankId)
            startActivity(intent)
        }

        // Size circle
        binding.tvTankSize.text = "${tank.size.toString()}l"

        // Tank image
        binding.ivHeaderImage.setOnClickListener { onTankImageClick(it) }
        if (tank.hasImage == true) {
            imageStorage.getImage(tank.tankId + ".jpg").addOnSuccessListener { uri ->
                ImageUtils.loadOnlineImageSource(this, binding.ivHeaderImage, uri)
            }
        }
    }

    private fun registerTankImageResult() {
        imageIntentLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageStorage = ImageStorage.getInstance()
                binding.pbImageUpload.visibility = View.VISIBLE

                imageStorage.uploadFile("${tank.tankId}.jpg", result.data!!.data!!)
                    .addOnFailureListener { exception ->
                        // TODO: Progress bar red?
                        Log.e(TAG, exception.toString())
                    }
                    .addOnSuccessListener { t ->
                        binding.pbImageUpload.visibility = View.INVISIBLE

                        // Update tank
                        if (tank.hasImage == false) {
                            tank.hasImage = true
                            tanksViewModel.updateTank(tank)
                        }

                        // Update header image preview
                        t.storage.downloadUrl.addOnSuccessListener { uri ->
                            ImageUtils.loadOnlineImageSource(
                                this, binding.ivHeaderImage,
                                uri
                            )
                        }
                    }
                    .addOnProgressListener { progress ->
                        binding.pbImageUpload.progress = (progress.bytesTransferred * 100 /
                                progress.totalByteCount).toInt()
                    }
            }
        }
    }

    private fun registerEditTankResult() {
        editTankIntentLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data

                    if (data != null) {
                        initializeUi(data)
                    }
                }
            }
    }

    private fun onTankImageClick(view: View) {
        ImageUtils.showImagePicker(imageIntentLauncher)
    }

    override fun onTankItemClick(tankItem: TankItem) {
        val intent = Intent(this, AddUpdateTankItemActivity::class.java)
        intent.putExtra("tank-id", tank.tankId)
        intent.putExtra("tank-item", tankItem)
        startActivity(intent)
    }

    override fun onTankItemLongPress(tankItem: TankItem): Boolean {
        val dialogClickListener =
            DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        if (!tank.tankId.isNullOrEmpty() &&
                            !tankItem.tankItemId.isNullOrEmpty()
                        ) {
                            tankItemsViewModel.removeTankItemById(
                                tank.tankId!!,
                                tankItem.tankItemId!!
                            )
                        }
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {}
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setTitle(R.string.are_you_sure)
            .setMessage(R.string.do_you_want_to_remove_tank_item)
            .setPositiveButton(R.string.yes, dialogClickListener)
            .setNegativeButton(R.string.cancel, dialogClickListener)
            .show()

        return true
    }
}