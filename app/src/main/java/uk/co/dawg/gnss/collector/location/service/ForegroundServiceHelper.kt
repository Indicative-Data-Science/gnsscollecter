package uk.co.dawg.gnss.collector.location.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import timber.log.Timber
import uk.co.dawg.gnss.collector.MainActivity
import uk.co.dawg.gnss.collector.R

interface ForegroundServiceHelper {

    fun getNotificationManager(): NotificationManager

    fun createNotificationChannel(
        context: Context,
        channelId: String = DEFAULT_CHANNEL_ID,
        channelName: String = DEFAULT_CHANNEL_ID
    ): String {

        val notificationService = getNotificationManager()

        if (notificationService.notificationChannels.none { it.id == channelId }) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_NONE
            ).apply {
                lightColor = Color.BLUE
                lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            }


            notificationService.createNotificationChannel(channel)

            Timber.i("Created new notification channel '$channelId'")
        }

        return channelId
    }

    fun createNotification(context: Context, channelId: String = DEFAULT_CHANNEL_ID): Notification {
        val pendingIntent: PendingIntent = Intent(context, MainActivity::class.java)
            .let { notificationIntent ->
                PendingIntent.getActivity(context, 0, notificationIntent, 0)
            }

        val pStopSelf = PendingIntent.getService(
            context,
            0,
            Intent(context, GnssCollectorService::class.java).apply {
                action = ACTION_STOP_SERVICE
            },
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(context.getText(R.string.app_name))
            .setContentText(context.getText(R.string.gnss_service_rationale))
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_my_location_24)
            .addAction(R.drawable.ic_location_disabled_24, "Stop", pStopSelf)
            .build()
    }

    companion object {
        private val DEFAULT_CHANNEL_ID = "gss-service"
        const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    }
}