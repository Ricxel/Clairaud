package com.omasba.clairaud.ui.components

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.time.delay

class AutoEqualizer {
    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        @Composable
        fun MusicNowPlayingUI(context: Context) {
            var songInfo by remember { mutableStateOf("Caricamento...") }
            var mediaController: MediaController? by remember { mutableStateOf(null) }
            val contextRef = rememberUpdatedState(context)

            // Funzione per aggiornare i metadati
            fun updateSongInfo(controller: MediaController?) {
                controller?.metadata?.let { metadata ->
                    songInfo = formatMetadata(metadata)
                } ?: run {
                    songInfo = "Nessuna canzone in riproduzione"
                }
            }

            // Funzione di polling per controllare periodicamente le sessioni
            LaunchedEffect(Unit) {
                val mediaSessionManager =
                    contextRef.value.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
                val componentName = ComponentName(contextRef.value, MyNotificationListener::class.java)

                while (true) {
                    val controllers = mediaSessionManager.getActiveSessions(componentName)

                    // Se trovi una sessione nuova o diversa, aggiorna
                    controllers.firstOrNull()?.let {
                        if (mediaController == null || mediaController != it) {
                            mediaController = it
                            updateSongInfo(it)

                            // Registriamo il callback per gli aggiornamenti in tempo reale
                            it.registerCallback(object : MediaController.Callback() {
                                override fun onMetadataChanged(metadata: MediaMetadata?) {
                                    metadata?.let {
                                        songInfo = formatMetadata(it)
                                    }
                                }
                            })
                        }
                    } ?: run {
                        mediaController = null
                        songInfo = "Nessuna canzone in riproduzione"
                    }

                    delay(java.time.Duration.ofMillis(5000)) // Polling ogni 5 secondi (deve essere dentro una coroutine)
                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Text(
                    text = songInfo,
                    fontSize = 20.sp
                )
            }
        }

        // Funzione per formattare i metadati
        fun formatMetadata(metadata: MediaMetadata): String {
            val title = metadata.getString(MediaMetadata.METADATA_KEY_TITLE) ?: "Sconosciuto"
            val artist = metadata.getString(MediaMetadata.METADATA_KEY_ARTIST) ?: "Artista sconosciuto"
            return "🎵 $title\n👤 $artist"
        }
        fun isNotificationServiceEnabled(context: Context): Boolean {
            val pkgName = context.packageName
            val enabledListeners = Settings.Secure.getString(
                context.contentResolver,
                "enabled_notification_listeners"
            )
            return enabledListeners?.contains(pkgName) == true
        }

        fun requestNotificationAccess(context: Context) {
            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            context.startActivity(intent)
        }

        fun getCurrentSongInfo(context: Context): String {
            val mediaSessionManager = context.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
            val componentName = ComponentName(context, MyNotificationListener::class.java)

            return try {
                val controllers: List<MediaController> = mediaSessionManager.getActiveSessions(componentName)

                for (controller in controllers) {
                    val metadata: MediaMetadata? = controller.metadata
                    if (metadata != null) {
                        val title = metadata.getString(MediaMetadata.METADATA_KEY_TITLE)
                        val artist = metadata.getString(MediaMetadata.METADATA_KEY_ARTIST)
                        return "🎵 $title\n👤 $artist"
                    }
                }

                "Nessuna canzone rilevata"
            } catch (e: SecurityException) {
                Log.e("MEDIA", "Permesso mancante: ${e.message}")
                "Permesso non concesso"
            }
        }
    }

}