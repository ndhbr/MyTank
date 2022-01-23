package de.ndhbr.mytank.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ToggleButton
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import de.ndhbr.mytank.R
import de.ndhbr.mytank.databinding.ActivityAddUpdateItemAlarmBinding
import de.ndhbr.mytank.models.ItemAlarm
import de.ndhbr.mytank.models.Tank
import de.ndhbr.mytank.models.TankItem
import de.ndhbr.mytank.utilities.Constants
import de.ndhbr.mytank.utilities.InjectorUtils
import de.ndhbr.mytank.utilities.ToastUtilities
import de.ndhbr.mytank.viewmodels.ItemAlarmViewModel
import java.util.*
import kotlin.collections.ArrayList

class AddUpdateItemAlarmActivity : AppCompatActivity() {

    private var _binding: ActivityAddUpdateItemAlarmBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ItemAlarmViewModel
    private var itemAlarm = ItemAlarm()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        _binding = ActivityAddUpdateItemAlarmBinding.inflate(layoutInflater)
        title = getString(R.string.ab_item_alarm)

        setContentView(binding.root)

        prefillInputs()
        initializeUi()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // Prefill item alarm for editing
    private fun prefillInputs() {
        if (intent.hasExtra(Constants.ACTIVITY_PARAM_ALARM)) {
            itemAlarm = intent.getParcelableExtra<ItemAlarm>(Constants.ACTIVITY_PARAM_ALARM)!!

            with(binding) {
                if (itemAlarm.days != null) {
                    if (itemAlarm.days!!.contains(Calendar.MONDAY))
                        dpItemAlertDays.dpMonday.isChecked = true
                    if (itemAlarm.days!!.contains(Calendar.TUESDAY))
                        dpItemAlertDays.dpTuesday.isChecked = true
                    if (itemAlarm.days!!.contains(Calendar.WEDNESDAY))
                        dpItemAlertDays.dpWednesday.isChecked = true
                    if (itemAlarm.days!!.contains(Calendar.THURSDAY))
                        dpItemAlertDays.dpThursday.isChecked = true
                    if (itemAlarm.days!!.contains(Calendar.FRIDAY))
                        dpItemAlertDays.dpFriday.isChecked = true
                    if (itemAlarm.days!!.contains(Calendar.SATURDAY))
                        dpItemAlertDays.dpSaturday.isChecked = true
                    if (itemAlarm.days!!.contains(Calendar.SUNDAY))
                        dpItemAlertDays.dpSunday.isChecked = true
                }
                etAddUpdateItemAlarmName.setText(itemAlarm.name)
                actvItemAlarmTank.setText(itemAlarm.tankName)
                actvItemAlarmTankItem.setText(itemAlarm.tankItemName)
                actvItemAlarmTime.setText(
                    resources.getStringArray(
                        R.array.hour_selection
                    )[itemAlarm.hour]
                )
                cbItemAlarmOddWeek.isChecked = itemAlarm.onlyOddWeeks == true
            }
        }
    }

    private fun initializeUi() {
        val factory = InjectorUtils.provideItemAlarmViewModelFactory()
        viewModel =
            ViewModelProvider(this, factory).get(ItemAlarmViewModel::class.java)

        buildDaySelection()
        buildNameInput()
        loadTankSpinner()
        loadHourSpinner()
        buildOddWeekCheckbox()
        buildSubmitButton()
    }

    // Day selection
    private fun buildDaySelection() {
        with(binding) {
            manageItemAlarmDaysArray(dpItemAlertDays.dpMonday, Calendar.MONDAY)
            manageItemAlarmDaysArray(dpItemAlertDays.dpTuesday, Calendar.TUESDAY)
            manageItemAlarmDaysArray(dpItemAlertDays.dpWednesday, Calendar.WEDNESDAY)
            manageItemAlarmDaysArray(dpItemAlertDays.dpThursday, Calendar.THURSDAY)
            manageItemAlarmDaysArray(dpItemAlertDays.dpFriday, Calendar.FRIDAY)
            manageItemAlarmDaysArray(dpItemAlertDays.dpSaturday, Calendar.SATURDAY)
            manageItemAlarmDaysArray(dpItemAlertDays.dpSunday, Calendar.SUNDAY)
        }
    }

    // Alarm name
    private fun buildNameInput() {
        binding.etAddUpdateItemAlarmName.doOnTextChanged { text, _, _, _ ->
            itemAlarm.name = text.toString()
        }
    }

    // Checks if day button has been toggled
    private fun manageItemAlarmDaysArray(toggleButton: ToggleButton, day: Int) {
        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (itemAlarm.days == null) {
                itemAlarm.days = ArrayList()
            }

            if (isChecked) {
                itemAlarm.days!!.add(day)
            } else {
                itemAlarm.days!!.remove(day)
            }
        }
    }

    // Dropdown spinner: Tank
    private fun loadTankSpinner() {
        with(binding) {
            viewModel.getTanksList { tanks ->
                ArrayAdapter(
                    this@AddUpdateItemAlarmActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    tanks
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    actvItemAlarmTank.setAdapter(adapter)

                    actvItemAlarmTank.setOnItemClickListener { parent, _, position, _ ->
                        val tank = parent.getItemAtPosition(position) as Tank
                        itemAlarm.tankId = tank.tankId
                        itemAlarm.tankName = tank.name
                        loadTankItemSpinner()
                        actvItemAlarmTankItem.clearListSelection()
                        actvItemAlarmTankItem.setText("")
                        tilItemAlarmTankItem.isEnabled = true
                    }
                }
            }
        }
    }

    // Dropdown spinner: Tank item
    private fun loadTankItemSpinner() {
        with(binding) {
            if (itemAlarm.tankId != null) {
                viewModel.getTankItemsList(itemAlarm.tankId!!) { tankItems ->
                    val placeholder = TankItem()
                    placeholder.name = "Choose whole tank"
                    val tankItemsList = listOf(placeholder) + tankItems

                    ArrayAdapter(
                        this@AddUpdateItemAlarmActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        tankItemsList
                    ).also { adapter ->
                        adapter.setDropDownViewResource(
                            android.R.layout.simple_spinner_dropdown_item
                        )
                        actvItemAlarmTankItem.setAdapter(adapter)

                        actvItemAlarmTankItem.setOnItemClickListener { parent, _, position, _ ->
                            val tankItem = parent.getItemAtPosition(position) as TankItem

                            if (tankItem.tankItemId != null) {
                                itemAlarm.tankItemId = tankItem.tankItemId
                                itemAlarm.tankItemName = tankItem.name
                            }
                        }
                    }
                }
            }

        }
    }

    // Dropdown spinner: hour
    private fun loadHourSpinner() {
        with(binding) {
            ArrayAdapter.createFromResource(
                this@AddUpdateItemAlarmActivity,
                R.array.hour_selection,
                android.R.layout.simple_spinner_dropdown_item
            ).also { adapter ->
                adapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item
                )
                actvItemAlarmTime.setAdapter(adapter)

                actvItemAlarmTime.setOnItemClickListener { _, _, position, _ ->
                    itemAlarm.hour = position
                }
            }
        }
    }

    // Checkbox: odd week
    private fun buildOddWeekCheckbox() {
        binding.cbItemAlarmOddWeek.setOnCheckedChangeListener { _, isChecked ->
            itemAlarm.onlyOddWeeks = isChecked
        }
    }

    // Button: submit
    private fun buildSubmitButton() {
        binding.btnAddItemAlarm.setOnClickListener {
            when {
                itemAlarm.days?.isEmpty() == true -> {
                    ToastUtilities.showShortToast(
                        this,
                        getString(R.string.form_error_day_input)
                    )
                }
                itemAlarm.name.isNullOrEmpty() -> {
                    ToastUtilities.showShortToast(
                        this,
                        getString(R.string.form_error_alarm_name_input)
                    )
                }
                itemAlarm.tankId.isNullOrEmpty() -> {
                    ToastUtilities.showShortToast(
                        this,
                        getString(R.string.form_error_alarm_tank_input)
                    )
                }

                else -> {
                    if (itemAlarm.itemAlarmId.isNullOrBlank()) {
                        viewModel.addItemAlarm(itemAlarm)
                    } else {
                        viewModel.updateItemAlarm(itemAlarm)
                    }

                    finish()
                }
            }
        }
    }
}