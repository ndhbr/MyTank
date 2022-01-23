package de.ndhbr.mytank.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.storage.UploadTask
import de.ndhbr.mytank.R
import de.ndhbr.mytank.data.ImageStorage
import de.ndhbr.mytank.databinding.ActivityTankItemBinding
import de.ndhbr.mytank.models.TankItem
import de.ndhbr.mytank.utilities.Constants
import de.ndhbr.mytank.utilities.ImageUtils
import de.ndhbr.mytank.utilities.InjectorUtils
import de.ndhbr.mytank.viewmodels.TankItemsViewModel
import java.text.SimpleDateFormat
import java.util.*

class TankItemActivity : AppCompatActivity() {

    // DEBUG
    private val TAG = "TANK_ITEM_ACTIVITY"

    // UI
    private lateinit var binding: ActivityTankItemBinding

    // Data
    private lateinit var viewModel: TankItemsViewModel
    private lateinit var editTankItemIntentLauncher:
            ActivityResultLauncher<Intent>

    // Images
    private val imageStorage = ImageStorage.getInstance()
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private var uploadTask: UploadTask? = null

    // Tank Item
    private var tankId: String = ""
    private var tankItem: TankItem = TankItem()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityTankItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent results
        registerTankItemImageResult()
        registerEditTankItemResult()

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
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tank_item_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.btn_edit_tank_item -> {
            // Send to update tank screen
            val intent = Intent(
                this@TankItemActivity,
                AddUpdateTankItemActivity::class.java
            )

            intent.putExtra(Constants.ACTIVITY_PARAM_TANK_ID, tankId)
            intent.putExtra(Constants.ACTIVITY_PARAM_TANK_ITEM, tankItem)

            editTankItemIntentLauncher.launch(intent)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun initializeUi(intent: Intent) {
        val factory = InjectorUtils.provideTankItemsViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(TankItemsViewModel::class.java)

        // Activity parameters
        handleActivityParameters(intent)

        with(binding) {
            // Name
            tvTankItemName.text = tankItem.name

            // Quantity
            tvTankItemQuantity.text = "${tankItem.count}x"

            // Insertion date
            val format = SimpleDateFormat("dd/MM/yyy", Locale.ENGLISH)
            tvTankItemInsertionDate.text = format.format(tankItem.existsSince!!)

            // Image
            ivTankItemHeaderImage.setOnClickListener {
                ImageUtils.showImagePicker(imageIntentLauncher)
            }
            if (tankItem.hasImage == true) {
                imageStorage.getImage(
                    String.format(
                        resources.getString(R.string.fb_storage_tank_items),
                        "${tankId}_${tankItem.tankItemId}"
                    )
                ).addOnSuccessListener { uri ->
                    ImageUtils.loadOnlineImageSource(
                        this@TankItemActivity,
                        ivTankItemHeaderImage, uri
                    )
                }
            }
        }
    }

    private fun handleActivityParameters(intent: Intent) {
        // To which tank the item belongs
        if (intent.hasExtra(Constants.ACTIVITY_PARAM_TANK_ID)) {
            tankId = intent.getStringExtra(Constants.ACTIVITY_PARAM_TANK_ID)!!
        }

        // Tank item
        if (intent.hasExtra(Constants.ACTIVITY_PARAM_TANK_ITEM)) {
            val types = resources.getStringArray(R.array.item_type)
            tankItem = intent
                .getParcelableExtra(Constants.ACTIVITY_PARAM_TANK_ITEM)!!
            supportActionBar?.title = types[tankItem.type?.ordinal!!]
        }
    }

    private fun registerTankItemImageResult() {
        imageIntentLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageStorage = ImageStorage.getInstance()
                binding.pbTankItemImageUpload.visibility = View.VISIBLE

                imageStorage.uploadFile(
                    String.format(
                        resources.getString(R.string.fb_storage_tank_items),
                        "${tankId}_${tankItem.tankItemId}"
                    ),
                    result.data!!.data!!
                )
                    .addOnFailureListener { exception ->
                        Log.e(TAG, exception.toString())
                    }
                    .addOnSuccessListener { t ->
                        binding.pbTankItemImageUpload.visibility = View.INVISIBLE

                        // Update tank
                        if (tankItem.hasImage == false) {
                            tankItem.hasImage = true
                            viewModel.updateTankItem(tankId, tankItem)
                        }

                        // Update header image preview
                        t.storage.downloadUrl.addOnSuccessListener { uri ->
                            ImageUtils.loadOnlineImageSource(
                                this, binding.ivTankItemHeaderImage,
                                uri
                            )
                        }
                    }
                    .addOnProgressListener { progress ->
                        binding.pbTankItemImageUpload.progress = (progress.bytesTransferred * 100 /
                                progress.totalByteCount).toInt()
                    }
            }
        }
    }

    private fun registerEditTankItemResult() {
        editTankItemIntentLauncher =
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
}