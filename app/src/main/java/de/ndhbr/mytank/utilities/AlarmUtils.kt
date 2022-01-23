package de.ndhbr.mytank.utilities

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import de.ndhbr.mytank.receiver.DailyBroadcastReceiver
import java.util.*

class AlarmUtils(private var context: Context) {

    init {
        createNotificationChannel()
    }

    // Alarm manager
    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE)
            as AlarmManager
    private val requestCode = 0

    // Sets the notification repeated alarm
    fun setAlarm() {
        val pendingIntent = getPendingIntent()

        val firstTime = Calendar.getInstance()
        firstTime[Calendar.HOUR_OF_DAY] = 8
        firstTime[Calendar.MINUTE] = 0
        firstTime[Calendar.SECOND] = 0

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP, firstTime.timeInMillis,
            AlarmManager.INTERVAL_HOUR, pendingIntent
        )
    }

    // Checks wether the repeated alarm is already registered
    fun isAlarmUpAndRunning(): Boolean {
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            Intent(),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
        ) != null
    }

    // Cancel the alarm
    fun cancelAlarm() {
        val pendingIntent = getPendingIntent()
        alarmManager.cancel(pendingIntent)
    }

    // Create pending intent
    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(this.context, DailyBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    // This initiates the required notification channel
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(
                NotificationManager::class.java
            )
            val name: CharSequence = "MyTankReminderChannel"
            val description = "Channel for Fish/Plant Alarms"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("MyTank", name, importance)
            channel.description = description

            notificationManager.createNotificationChannel(channel)
        }
    }
}