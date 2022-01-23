package de.ndhbr.mytank.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import de.ndhbr.mytank.databinding.ActivityOverviewBinding
import de.ndhbr.mytank.R

class OverviewActivity : AppCompatActivity() {

    // View binding
    private lateinit var binding: ActivityOverviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeUi()
    }

    private fun initializeUi() {
        // Tabbar
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    changeFragment(TanksListFragment())
                    true
                }
                R.id.page_2 -> {
                    changeFragment(AlarmFragment())
                    true
                }
                R.id.page_3 -> {
                    changeFragment(MoreFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_tanks_list, fragment)
            .commit()
    }
}