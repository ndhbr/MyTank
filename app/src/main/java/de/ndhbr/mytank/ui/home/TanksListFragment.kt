package de.ndhbr.mytank.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import de.ndhbr.mytank.R
import de.ndhbr.mytank.adapters.TankListAdapter
import de.ndhbr.mytank.databinding.FragmentTanksListBinding
import de.ndhbr.mytank.interfaces.TankListener
import de.ndhbr.mytank.models.Tank
import de.ndhbr.mytank.utilities.InjectorUtils
import de.ndhbr.mytank.viewmodels.TanksViewModel
import kotlin.collections.ArrayList
import android.content.DialogInterface

class TanksListFragment : Fragment(R.layout.fragment_tanks_list), TankListener {

    // View binding
    private var _binding: FragmentTanksListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TanksViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTanksListBinding.inflate(inflater, container, false)

        initializeUi()

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initializeUi() {
        val factory = InjectorUtils.provideTanksViewModelFactory()
        viewModel =
            ViewModelProvider(this, factory).get(TanksViewModel::class.java)
        val recyclerView = binding.rvList
        val tankAdapter = TankListAdapter(ArrayList(), this)

        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = tankAdapter

        viewModel.getTanks().observe(viewLifecycleOwner, { tanks ->
            tankAdapter.updateData(tanks as ArrayList<Tank>)
        })

        // button setonclicklistener...
        binding.fabAddTest.setOnClickListener {
            val intent = Intent(activity, AddUpdateTankActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onTankClick(tank: Tank) {
        val launcherIntent = Intent(activity, TankActivity::class.java)
        launcherIntent.putExtra("tank", tank)
        startActivity(launcherIntent)
    }

    override fun onTankLongPress(tank: Tank): Boolean {
        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        tank.tankId?.let { viewModel.removeTankById(it) }
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {}
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder
            .setTitle(R.string.are_you_sure)
            .setMessage(R.string.do_you_want_to_delete_this_tank)
            .setPositiveButton(R.string.yes, dialogClickListener)
            .setNegativeButton(R.string.cancel, dialogClickListener)
            .show()

        return true
    }
}