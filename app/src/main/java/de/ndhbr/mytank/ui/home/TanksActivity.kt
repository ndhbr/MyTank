package de.ndhbr.mytank.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.ndhbr.mytank.R
import de.ndhbr.mytank.adapters.TankListAdapter
import de.ndhbr.mytank.models.Tank
import de.ndhbr.mytank.uitilities.InjectorUtils
import de.ndhbr.mytank.viewmodels.TanksViewModel
import kotlinx.android.synthetic.main.activity_tanks.*
import java.lang.StringBuilder

class TanksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tanks)

        initializeUi()
    }

    private fun initializeUi() {
        val factory = InjectorUtils.provideTanksViewModelFactory()
        val viewModel =
            ViewModelProvider(this@TanksActivity, factory).get(TanksViewModel::class.java)
        val recyclerView = findViewById<RecyclerView>(R.id.rv_list)
        val tankAdapter = TankListAdapter(ArrayList<Tank>())

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = tankAdapter

        viewModel.getTanks().observe(this@TanksActivity, Observer { tanks ->
            tankAdapter.updateData(tanks as ArrayList<Tank>)
        })

        // button setonclicklistener...
        fab_add_test.setOnClickListener { l ->
            val tank = Tank("Vallah billa")
            viewModel.addTank(tank)
        }
    }
}