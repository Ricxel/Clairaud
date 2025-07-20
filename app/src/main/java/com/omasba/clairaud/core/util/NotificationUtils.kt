package com.omasba.clairaud.core.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.omasba.clairaud.R

class NotificationUtils(
    private val channelId: String,
) {

    /**
     * Create a notification object
     * @param context Application context
     * @param onGoing True if it is a permanent notification, false otherwise
     * @param contentTitle Notification's title
     * @param contentText Notification's text
     * @param ticker Notification's ticker
     * @return The notification object with the specified parameters
     */
    fun createNotification(context: Context,
                           onGoing: Boolean,
                           contentTitle: String,
                           contentText: String,
                           ticker: String,
                           ): Notification {
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(onGoing)
            .setTicker(ticker)

        Log.d("MusicDetection", "Notifica creata")

        return notificationBuilder.build()
    }

    /**
     * Create a notification channel with the specified channel ID
     * @param context Application context
     * @param channelName Name of the channel
     * @param description Description of the channel
     */
    fun createNotificationChannel(
        context: Context,
        channelName: String,
        description: String,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            channel.description = description

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            manager.createNotificationChannel(channel)
        }
    }


    /**
     * Send a notification in the created channel
     * @param context Application context
     * @param notification The notification to send
     * @param notificationId Notification ID
     */
    fun sendNotification(
        context: Context,
        notification: Notification,
        notificationId: Int
    ) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)
    }

    /**
     * Cancels a notification
     * @param context the context of the notification
     * @param notificationId the id of the notification to cancel
     */
    fun cancelNotification(
        context: Context,
        notificationId: Int
    ){
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(notificationId)
    }
}