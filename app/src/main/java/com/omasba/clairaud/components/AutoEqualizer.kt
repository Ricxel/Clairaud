package com.omasba.clairaud.components

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import com.google.gson.GsonBuilder
import com.omasba.clairaud.ui.components.TrackTagsResponse
import com.omasba.clairaud.ui.components.TrackTagsResponseDeserializer
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder

class AutoEqualizer {
    companion object{
//        @RequiresApi(Build.VERSION_CODES.O)
//        @Composable
//        fun MusicNowPlayingUI(context: Context) {
//            var songInfo by remember { mutableStateOf("Caricamento...") }
//            var mediaController: MediaController? by remember { mutableStateOf(null) }
//            val contextRef = rememberUpdatedState(context)
//
//            // Funzione per aggiornare i metadati
//            fun updateSongInfo(controller: MediaController?) {
//                controller?.metadata?.let { metadata ->
//                    songInfo = formatMetadata(metadata)
//                } ?: run {
//                    songInfo = "Nessuna canzone in riproduzione"
//                }
//            }
//
//            // Funzione di polling per controllare periodicamente le sessioni
//            LaunchedEffect(Unit) {
//                val mediaSessionManager =
//                    contextRef.value.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
//                val componentName = ComponentName(contextRef.value, MyNotificationListener::class.java)
//
//                while (true) {
//                    val controllers = mediaSessionManager.getActiveSessions(componentName)
//
//                    // Se trovi una sessione nuova o diversa, aggiorna
//                    controllers.firstOrNull()?.let {
//                        if (mediaController == null || mediaController != it) {
//                            mediaController = it
//                            updateSongInfo(it)
//
//                            // Registriamo il callback per gli aggiornamenti in tempo reale
//                            it.registerCallback(object : MediaController.Callback() {
//                                override fun onMetadataChanged(metadata: MediaMetadata?) {
//                                    metadata?.let {
//                                        songInfo = formatMetadata(it)
//                                    }
//                                }
//                            })
//                        }
//                    } ?: run {
//                        mediaController = null
//                        songInfo = "Nessuna canzone in riproduzione"
//                    }
//
//                    delay(java.time.Duration.ofMillis(5000)) // Polling ogni 5 secondi (deve essere dentro una coroutine)
//                }
//            }
//
//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(24.dp)
//            ) {
//                Text(
//                    text = songInfo,
//                    fontSize = 20.sp
//                )
//            }
//        }

//        // Funzione per formattare i metadati
//        fun formatMetadata(metadata: MediaMetadata): String {
//            val title = metadata.getString(MediaMetadata.METADATA_KEY_TITLE) ?: "Sconosciuto"
//            val artist = metadata.getString(MediaMetadata.METADATA_KEY_ARTIST) ?: "Artista sconosciuto"
//            return "🎵 $title\n👤 $artist"
//        }
        val API_KEY:String = "51b710eed01b56a851a342cca1bead2a"
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
        fun getTrackTags(artist: String, track: String, apiKey: String): Set<String> {
            val client = OkHttpClient()
            //metto il mio deserializzatore personalizzato per avere subito i tag
            val gson = GsonBuilder()
                .registerTypeAdapter(TrackTagsResponse::class.java, TrackTagsResponseDeserializer())
                .create()


            val encodedArtist = URLEncoder.encode(artist, "UTF-8")
            val encodedTrack = URLEncoder.encode(track, "UTF-8")

            val url = "https://ws.audioscrobbler.com/2.0/?" +
                    "method=track.gettoptags" +
                    "&artist=$encodedArtist" +
                    "&track=$encodedTrack" +
                    "&api_key=$apiKey" +
                    "&format=json"

            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw Exception("HTTP error: ${response.code}")

                val body = response.body?.string() ?: return emptySet()

                val json = gson.fromJson(body, TrackTagsResponse::class.java)

                val tagSet = json.tags //ottengo i tag direttamente in un set<String> perchè sto usando il sezializzatore custom
                Log.d("TrackTags", "$tagSet")

                return tagSet
            }
        }

        fun getPresetToApply(artist: String, track: String, apiKey: String, availableTags: Set<String>): Set<String>{
            val songTags = getTrackTags(artist, track, apiKey)
            return songTags intersect availableTags
        }

//
//        fun getCurrentSongInfo(context: Context): String {
//            val mediaSessionManager = context.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
//            val componentName = ComponentName(context, MyNotificationListener::class.java)
//
//            return try {
//                val controllers: List<MediaController> = mediaSessionManager.getActiveSessions(componentName)
//
//                for (controller in controllers) {
//                    val metadata: MediaMetadata? = controller.metadata
//                    if (metadata != null) {
//                        val title = metadata.getString(MediaMetadata.METADATA_KEY_TITLE)
//                        val artist = metadata.getString(MediaMetadata.METADATA_KEY_ARTIST)
//                        return "🎵 $title\n👤 $artist"
//                    }
//                }
//
//                "Nessuna canzone rilevata"
//            } catch (e: SecurityException) {
//                Log.e("MEDIA", "Permesso mancante: ${e.message}")
//                "Permesso non concesso"
//            }
//        }
    }

}