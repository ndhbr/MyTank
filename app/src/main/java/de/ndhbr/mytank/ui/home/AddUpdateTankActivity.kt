package de.ndhbr.mytank.ui.home

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import de.ndhbr.mytank.R
import de.ndhbr.mytank.databinding.ActivityAddTankBinding
import de.ndhbr.mytank.models.Tank
import de.ndhbr.mytank.utilities.DateUtils
import de.ndhbr.mytank.utilities.InjectorUtils
import de.ndhbr.mytank.utilities.ToastUtilities
import de.ndhbr.mytank.viewmodels.TanksViewModel
import java.util.*

class AddUpdateTankActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTankBinding
    private var editTank: Tank = Tank()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityAddTankBinding.inflate(layoutInflater)
        title = getString(R.string.ab_tank)

        setContentView(binding.root)

        // Prefill tank for editing
        if (intent.hasExtra("tank")) {
            editTank = intent.getParcelableExtra<Tank>("tank")!!

            with(binding) {
                tvHeadline.text = resources.getText(R.string.edit_your_fish_tank)
                etTankName.setText(editTank.name)
                editTank.size?.let { etTankSize.setText(it.toString()) }
                editTank.existsSince?.let {
                    etTankExistingSince.setText(
                        DateUtils.humanReadableDate(it)
                    )
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
        val factory = InjectorUtils.provideTanksViewModelFactory()
        val viewModel =
            ViewModelProvider(this, factory).get(TanksViewModel::class.java)
        val newTank = editTank.copy()

        with(binding) {
            etTankName.doOnTextChanged { text, _, _, _ ->
                newTank.name = text.toString()
            }
            etTankSize.doOnTextChanged { text, _, _, _ ->
                if (!text.isNullOrBlank()) {
                    newTank.size = text.toString().toInt()
                } else {
                    newTank.size = 1
                }
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
                    newTank.existsSince = date
                    etTankExistingSince.setText(
                        DateUtils.humanReadableDate(date)
                    )
                }

            // Pick date dialog
            etTankExistingSince.setOnClickListener {
                val dialog =
                    DatePickerDialog(this@AddUpdateTankActivity, dateSetListener, year, month, day)
                dialog.datePicker.maxDate = Date().time
                dialog.show()
            }

            // Save button
            btnAddTank.setOnClickListener {
                when {
                    newTank.name.isNullOrEmpty() -> {
                        ToastUtilities.showShortToast(
                            this@AddUpdateTankActivity,
                            getString(R.string.form_error_tank_name_input)
                        )
                    }
                    (newTank.size == null || newTank.size!! <= 0) -> {
                        ToastUtilities.showShortToast(
                            this@AddUpdateTankActivity,
                            getString(R.string.form_error_tank_size_input)
                        )
                    }
                    (newTank.existsSince == null) -> {
                        ToastUtilities.showShortToast(
                            this@AddUpdateTankActivity,
                            getString(R.string.form_error_set_up_date_input)
                        )
                    }

                    else -> {
                        if (!newTank.tankId.isNullOrEmpty()) {
                            viewModel.updateTank(newTank)
                        } else {
                            viewModel.addTank(newTank)
                        }

                        finish()
                    }
                }
            }
        }
    }
}