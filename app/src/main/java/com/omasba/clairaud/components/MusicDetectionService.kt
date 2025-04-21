package com.omasba.clairaud.components

import android.content.ComponentName
import android.content.Intent
import android.media.MediaMetadata
import android.media.session.MediaSessionManager
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build
import androidx.compose.runtime.saveable.autoSaver
import com.omasba.clairaud.ui.components.AutoEqRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MusicDetectionService : NotificationListenerService() {

    private lateinit var sessionManager: MediaSessionManager
    private var lastTitle: String = ""
    private var lastArtist: String = ""
    private val NOTIFICATION_ID = 1001
    private val CHANNEL_ID = "music_detection_channel"

    override fun onCreate() {
        super.onCreate()
        Log.d("MusicDetection", "Service created")
        sessionManager = getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
        createNotificationChannel()

        // Avvia il servizio come foreground per mantenerlo attivo in background
//        startForeground(NOTIFICATION_ID, createForegroundNotification())
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("MusicDetection", "Listener connected")
        // Avvia il polling delle sessioni media
        checkForActiveSessions()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Rilevamento Musica",
                NotificationManager.IMPORTANCE_LOW
            )
            channel.description = "Notifiche per il rilevamento della musica in riproduzione"
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkForActiveSessions() {
        Thread {
            try {
                while (true) {
                    try {
                        val component = ComponentName(this, javaClass)
                        val controllers = sessionManager.getActiveSessions(component)

                        var foundMusic = false
                        for (controller in controllers) {
                            val metadata = controller.metadata
                            if (metadata != null) {
                                val title = metadata.getString(MediaMetadata.METADATA_KEY_TITLE) ?: ""
                                val artist = metadata.getString(MediaMetadata.METADATA_KEY_ARTIST) ?:
                                metadata.getString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST) ?: ""

                                if (title.isNotEmpty() && (title != lastTitle || artist != lastArtist)) {
                                    //se è diversa dalla traccia precedente, posso procedere con il call dell'api per capire il genere
                                    Log.d("MusicDetection", "Detected: $title by $artist")

                                    val response = AutoEqRepo.updateGenre(artist = artist, title = title)
                                    Log.d("MusicDetection", "Response: $response")

                                    lastTitle = title
                                    lastArtist = artist
                                    AutoEqRepo.title = title
                                    AutoEqRepo.artist = artist

                                    showMusicNotification(title, artist, response)
                                }

                                foundMusic = true
                                break
                            }
                        }

                        if (!foundMusic && lastTitle.isNotEmpty()) {
                            // Nessuna musica attiva, resetta lo stato
                            lastTitle = ""
                            lastArtist = ""
                        }
                    } catch (e: Exception) {
                        Log.e("MusicDetection", "Error checking sessions", e)
                    }

                    // Aspetta 5 secondi prima del prossimo controllo
//                    Log.d("MusicDetection", "Waiting...")
                    Thread.sleep(5000)
                }
            } catch (e: InterruptedException) {
                Log.d("MusicDetection", "Music detection thread interrupted")
            }
        }.start()
    }

    private fun showMusicNotification(title: String, artist: String, genre: String) {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("🎵 In riproduzione")
            .setContentText("$title - $genre")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(false)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MusicDetection", "Service destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }
}