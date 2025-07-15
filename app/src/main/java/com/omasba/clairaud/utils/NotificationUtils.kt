package com.omasba.clairaud.utils

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

    //crea l'oggetto per la notifica
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
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(onGoing)
            .setTicker(ticker)

        Log.d("MusicDetection", "Notifica creata")

        return notificationBuilder.build()
    }

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


    fun sendNotification(
        context: Context,
        notification: Notification,
        notificationId: Int
    ) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)
    }


    fun cancelNotification(
        context: Context,
        notificationId: Int
    ){
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(notificationId)
    }
}