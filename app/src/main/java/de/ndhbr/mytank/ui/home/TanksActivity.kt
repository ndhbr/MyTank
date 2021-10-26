package de.ndhbr.mytank.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import de.ndhbr.mytank.R
import de.ndhbr.mytank.uitilities.InjectorUtils
import java.lang.StringBuilder

class TanksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tanks)

        initializeUi()
    }

    private fun initializeUi() {
        val factory = InjectorUtils.provideQuotesViewModelFactory()
        val viewModel =
            ViewModelProvider(this@TanksActivity, factory).get(TanksViewModel::class.java)

        viewModel.getTanks().observe(this@TanksActivity, Observer { tanks ->
            val stringBuilder = StringBuilder()
            tanks.forEach {
                tank -> stringBuilder.append("$tank\n\n")
            }
            // textView.text = stringBuilder.toString()
        })

        // button setonclicklistener...
    }
}