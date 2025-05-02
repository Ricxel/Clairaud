package com.omasba.clairaud.components

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.audiofx.AudioEffect
import android.media.audiofx.Equalizer
import android.os.Build
import android.app.*
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi

class EqService : Service() {

    private val TAG = "EqService"
    private val audioSessionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "Receive")
            if (intent == null) return
            val sessionId = intent.getIntExtra(AudioEffect.EXTRA_AUDIO_SESSION, AudioEffect.ERROR)
            if (sessionId == AudioEffect.ERROR) return
            var equalizer: Equalizer? = null
            when (intent.action) {
                AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION -> {
                    Log.d(TAG, "Audio session opened: $sessionId")
                    try {
                        equalizer = Equalizer(0, sessionId)
                        equalizer.enabled = true

//                        val bands = equalizer.numberOfBands
//                        val range = equalizer.bandLevelRange
//                        val midLevel = ((range[0] + range[1]) / 2).toShort()
//                        for (i in 0 until bands) {
//                            equalizer.setBandLevel(i.toShort(), midLevel)
//                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Equalizer error: ${e.message}")
                    }
                }

                AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION -> {
                    Log.d(TAG, "Audio session closed: $sessionId")
                    // (Opzionalmente) rilascia l’equalizer
                    equalizer?.release()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        Log.d(TAG, "Servizio Creato")
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundServiceWithNotification()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(audioSessionReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // servizio non bound
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForegroundServiceWithNotification() {
        val channelId = "eq_channel"
        val channelName = "Equalizer Service"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(chan)
        }

        val notification = Notification.Builder(this, channelId)
            .setContentTitle("Equalizer attivo")
            .setContentText("In ascolto delle sessioni audio...")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true)
            .build()

        startForeground(1, notification)
    }
}
