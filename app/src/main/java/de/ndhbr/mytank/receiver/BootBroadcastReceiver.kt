package de.ndhbr.mytank.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import de.ndhbr.mytank.utilities.AlarmUtils

class BootBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val alarmUtils = AlarmUtils(context)
            if (!alarmUtils.isAlarmUpAndRunning()) {
                alarmUtils.setAlarm()
            }
        }
    }
}