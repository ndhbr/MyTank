package de.ndhbr.mytank.receiver

import android.app.PendingIntent
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import de.ndhbr.mytank.R
import de.ndhbr.mytank.data.Database
import de.ndhbr.mytank.ui.home.OverviewActivity


class DailyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val database = Database.getInstance()
        createNotification(context, database.authDao.isLoggedIn())
    }

    private fun createNotification(context: Context?, isLoggedIn: Boolean) {
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
            .setContentTitle("Aquarium")
            .setContentText("Schnell, füttere deine Fische! Er ist eingeloggt: $isLoggedIn")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
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
        notificationManager.notify(123, builder.build())
    }
}