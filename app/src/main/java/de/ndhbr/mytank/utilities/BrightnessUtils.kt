package de.ndhbr.mytank.utilities

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class BrightnessUtils {

    // Shared preferences
    private val sharedPreferencesName = "mytank_prefs"
    private val brightnessSettingsKey = "BRIGHTNESS"

    // Get brightness state of app
    fun getBrightnessState(context: Context): BrightnessState {
        val sharedPref = context.getSharedPreferences(
            sharedPreferencesName, Context.MODE_PRIVATE
        )

        return BrightnessState.valueOf(
            sharedPref.getString(brightnessSettingsKey, BrightnessState.Dark.name)!!
        )
    }

    // Set brightness state of app
    fun setBrightnessState(context: Context, brightnessState: BrightnessState) {
        val sharedPref = context.getSharedPreferences(
            sharedPreferencesName, Context.MODE_PRIVATE
        )

        when (brightnessState) {
            BrightnessState.System -> AppCompatDelegate
                .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            BrightnessState.Dark -> AppCompatDelegate
                .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            BrightnessState.Light -> AppCompatDelegate
                .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        sharedPref
            .edit()
            .putString(brightnessSettingsKey, brightnessState.name)
            .apply()
    }

    enum class BrightnessState {
        System,
        Dark,
        Light
    }
}