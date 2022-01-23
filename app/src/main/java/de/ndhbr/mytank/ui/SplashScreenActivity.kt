package de.ndhbr.mytank.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.ndhbr.mytank.R
import de.ndhbr.mytank.data.AuthDao
import de.ndhbr.mytank.ui.auth.LoginActivity
import de.ndhbr.mytank.ui.home.OverviewActivity
import de.ndhbr.mytank.utilities.BrightnessUtils

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authDao = AuthDao()
        initialize()

        if (authDao.isLoggedIn()) {
            startActivity(Intent(this, OverviewActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        finish()
    }

    private fun initialize() {
        // Status bar
        window.statusBarColor = getColor(R.color.purple_700)

        // Brightness
        val brightnessUtils = BrightnessUtils()
        brightnessUtils.setBrightnessState(
            this@SplashScreenActivity,
            brightnessUtils.getBrightnessState(this@SplashScreenActivity)
        )
    }
}