package de.ndhbr.mytank.ui.home

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import de.ndhbr.mytank.R
import de.ndhbr.mytank.databinding.ActivityAddUpdateTankItemBinding
import de.ndhbr.mytank.enum.TankItemType
import de.ndhbr.mytank.models.TankItem
import de.ndhbr.mytank.utilities.DateUtils
import de.ndhbr.mytank.utilities.InjectorUtils
import de.ndhbr.mytank.viewmodels.TankItemsViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddUpdateTankItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUpdateTankItemBinding
    private var tankId: String = ""
    private var editTankItem: TankItem = TankItem()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityAddUpdateTankItemBinding.inflate(layoutInflater)
        title = getString(R.string.ab_tank_item)

        setContentView(binding.root)

        // To which tank should the item belong
        if (intent.hasExtra("tank-id")) {
            tankId = intent.getStringExtra("tank-id")!!
        }

        // Prefill tank item for editing
        if (intent.hasExtra("tank-item")) {
            editTankItem = intent.getParcelableExtra<TankItem>("tank-item")!!

            with(binding) {
                etAddUpdateTankItemName.setText(editTankItem.name)
                editTankItem.count.let { etAddUpdateTankItemCount.setText(it.toString()) }
                editTankItem.existsSince?.let {
                    etAddUpdateTankItemExistingSince.setText(
                        DateUtils.humanReadableDate(it)
                    )
                }
                editTankItem.type?.let {
                    val types = resources.getStringArray(R.array.item_type)
                    actvTankItemType.setText(types[it.ordinal])
                }
            }
        }

        initializeUi()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initializeUi() {
        val factory = InjectorUtils.provideTankItemsViewModelFactory()
        val viewModel =
            ViewModelProvider(this, factory).get(TankItemsViewModel::class.java)
        val newTankItem = editTankItem.copy()

        with(binding) {
            etAddUpdateTankItemName.doOnTextChanged { text, _, _, _ ->
                newTankItem.name = text.toString()
            }
            etAddUpdateTankItemCount.doOnTextChanged { text, _, _, _ ->
                if (!text.isNullOrBlank()) {
                    newTankItem.count = text.toString().toInt()
                } else {
                    newTankItem.count = 1
                }
            }

            // Dropdown spinner
            ArrayAdapter.createFromResource(
                this@AddUpdateTankItemActivity,
                R.array.item_type,
                android.R.layout.simple_spinner_dropdown_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                actvTankItemType.setAdapter(adapter)
            }
            actvTankItemType.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    newTankItem.type = TankItemType.values()[position]
                }

            // Initial date
            val c = Calendar.getInstance()
            var year = c.get(Calendar.YEAR)
            var month = c.get(Calendar.MONTH)
            var day = c.get(Calendar.DAY_OF_MONTH)

            // Date listener
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { _, _year, _month, _dayOfMonth ->
                    year = _year
                    month = _month
                    day = _dayOfMonth

                    c.set(year, month, day)

                    val date = Date.from(c.toInstant())
                    newTankItem.existsSince = date

                    etAddUpdateTankItemExistingSince.setText(
                        DateUtils.humanReadableDate(date)
                    )
                }

            // Pick date dialog
            etAddUpdateTankItemExistingSince.setOnClickListener {
                val dialog =
                    DatePickerDialog(
                        this@AddUpdateTankItemActivity,
                        dateSetListener,
                        year,
                        month,
                        day
                    )
                dialog.datePicker.maxDate = Date().time
                dialog.show()
            }

            // Save button
            btnAddTankItem.setOnClickListener {
                if (tankId.isNotEmpty()) {
                    if (!newTankItem.tankItemId.isNullOrEmpty()) {
                        viewModel.updateTankItem(tankId, newTankItem)
                    } else {
                        viewModel.addTankItem(tankId, newTankItem)
                    }

                    finish()
                    // TODO: Finish with result, update data
                }
            }
        }
    }
}