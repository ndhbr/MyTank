package de.ndhbr.mytank.ui.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import de.ndhbr.mytank.R
import de.ndhbr.mytank.adapters.ItemAlarmListAdapter
import de.ndhbr.mytank.databinding.FragmentAlarmBinding
import de.ndhbr.mytank.interfaces.ItemAlarmListener
import de.ndhbr.mytank.models.ItemAlarm
import de.ndhbr.mytank.utilities.InjectorUtils
import de.ndhbr.mytank.viewmodels.ItemAlarmViewModel

class AlarmFragment : Fragment(R.layout.fragment_alarm), ItemAlarmListener {

    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ItemAlarmViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlarmBinding.inflate(inflater, container, false)
        activity?.title = getString(R.string.ab_alarms)
        initializeUi()

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initializeUi() {
        val factory = InjectorUtils.provideItemAlarmViewModelFactory()
        viewModel = ViewModelProvider(this@AlarmFragment, factory)
            .get(ItemAlarmViewModel::class.java)
        val recyclerView = binding.rvAlarmList
        val alarmItemAdapter = ItemAlarmListAdapter(ArrayList(), this)

        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = alarmItemAdapter

        viewModel.getItemAlarms().observe(viewLifecycleOwner, { itemAlarms ->
            alarmItemAdapter.updateData(itemAlarms as ArrayList<ItemAlarm>)
        })

        with(binding) {
            // New item alarm button
            fabAddAlarm.setOnClickListener {
                val intent = Intent(
                    activity,
                    AddUpdateItemAlarmActivity::class.java
                )
                startActivity(intent)
            }
        }
    }

    override fun onItemAlarmClick(itemAlarm: ItemAlarm) {
        val intent = Intent(activity, AddUpdateItemAlarmActivity::class.java)
        intent.putExtra("alarm", itemAlarm)
        startActivity(intent)
    }

    override fun onItemAlarmLongPress(itemAlarm: ItemAlarm): Boolean {
        val dialogClickListener =
            DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        if (!itemAlarm.itemAlarmId.isNullOrEmpty()) {
                            viewModel.removeItemAlarmById(
                                itemAlarm.itemAlarmId!!
                            )
                        }
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {}
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder
            .setTitle(R.string.are_you_sure)
            .setMessage("Do you really want to remove this alarm?")
            .setPositiveButton(R.string.yes, dialogClickListener)
            .setNegativeButton(R.string.cancel, dialogClickListener)
            .show()

        return true
    }
}