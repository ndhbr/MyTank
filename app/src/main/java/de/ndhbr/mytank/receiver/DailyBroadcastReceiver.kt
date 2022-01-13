package de.ndhbr.mytank.receiver

import android.app.PendingIntent
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import de.ndhbr.mytank.R
import de.ndhbr.mytank.data.AuthDao
import de.ndhbr.mytank.data.Database
import de.ndhbr.mytank.ui.home.OverviewActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class DailyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (!AuthDao().isLoggedIn()) {
            return
        }

        val database = Database.getInstance()

        GlobalScope.launch {
            val alarms = database.itemAlarmDao.getCurrentAlarmsList()

            for (alarm in alarms) {
                if (alarm.onlyOddWeeks == true &&
                    Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) % 2 == 0
                ) {
                    continue;
                }

                val title = alarm.tankName ?: context.resources.getString(
                    R.string.notification_default_title
                )
                val subtitle = alarm.name ?: context.resources.getString(
                    R.string.notification_default_subtitle
                )

                createNotification(context, alarms.indexOf(alarm),
                    title, subtitle)
            }
        }
    }

    private fun createNotification(
        context: Context?, id: Int, title: String,
        subtitle: String
    ) {
        val i = Intent(context, OverviewActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent =
            PendingIntent.getActivity(
                context, 0, i,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        val builder = NotificationCompat.Builder(
            context!!, "MyTank"
        ).setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(subtitle)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.ic_baseline_alarm_24)
            .setContentIntent(pendingIntent)
            .setVibrate(
                longArrayOf(
                    1000,
                    1000,
                    1000,
                    1000,
                    1000
                )
            )

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(id, builder.build())
    }
}