package com.omasba.clairaud.service.eq

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.audiofx.AudioEffect
import android.os.Build
import android.os.IBinder
import android.provider.MediaStore.Audio
import android.util.Log
import androidx.annotation.RequiresApi
import com.omasba.clairaud.data.repository.EqRepo
import com.omasba.clairaud.core.util.NotificationUtils

/**
 * Service that identify audio session ids of active audio sessions
 */
class EqService : Service() {

    private val TAG = "EqService"
    private val CHANNEL_ID = "equalizer_service_channel"
    private val NOTIFICATION_ID = 1002 //id per la notifica permanente del servizio
    private val notificationUtils = NotificationUtils(CHANNEL_ID) //per gestire la notifica permanente


    private val audioSessionReceiver = AudioSessionReceiver()


    @RequiresApi(Build.VERSION_CODES.O)
    /**
     * Initialize the event receiver
     */
    override fun onCreate() {
        Log.d(TAG, "Service active")
        super.onCreate()
        registerReceiver(
            audioSessionReceiver,
            IntentFilter().apply {
                addAction(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION)
                addAction(AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION)
            },
            Context.RECEIVER_EXPORTED
        )
    }

    /**
     * It start the service as a foreground one calling the proper function
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundServiceWithNotification()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        audioSessionReceiver.releaseEq() //tolgo l'eq
        unregisterReceiver(audioSessionReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // servizio non bound
    }

    /**
     * Start the service as a foreground one, and create the associated notification
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForegroundServiceWithNotification() {
        //creo il channel per le notifiche del servizio
        notificationUtils.createNotificationChannel(
            this,
            "Equalizer Service",
            "Channel for Clairaud's equalizer service"
        )

        //creo la notifica
        val notification = notificationUtils.createNotification(
            this,
            true, //per renderla permanente
            "Equalizer service",
            "Listening for audio sessions",
            "Clairaud equalizer service"
        )

        //faccio partire il servizio in foreground con la notifica permanente
        startForeground(NOTIFICATION_ID, notification)
    }
}
