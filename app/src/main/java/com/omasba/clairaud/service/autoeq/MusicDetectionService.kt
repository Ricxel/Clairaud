package com.omasba.clairaud.service.autoeq

import android.content.ComponentName
import android.content.Intent
import android.media.MediaMetadata
import android.media.session.MediaSessionManager
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.content.Context
import android.util.Log
import com.omasba.clairaud.service.autoeq.presentation.state.AutoEqStateHolder
import com.omasba.clairaud.data.repository.EqRepo
import com.omasba.clairaud.presentation.store.state.Tag
import com.omasba.clairaud.core.network.API_KEY
import com.omasba.clairaud.core.network.LastFmApi
import com.omasba.clairaud.data.repository.UserRepo
import com.omasba.clairaud.core.util.NotificationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class MusicDetectionService : NotificationListenerService() {
    private var pollingJob: Job? = null

    private fun isPollingActive() = pollingJob != null
    private fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
        lastTitle = ""
        lastArtist = ""
    }

    private lateinit var sessionManager: MediaSessionManager
    private var lastTitle: String = ""
    private var lastArtist: String = ""
    private val NOTIFICATION_ID = 1001 //id della notifica che mostrerà il brano rilevato in quel momento
    private val CHANNEL_ID = "music_detection_channel" //id del canale di notifiche del servizio
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val notificationUtils: NotificationUtils = NotificationUtils(CHANNEL_ID)

    override fun onCreate() {
        super.onCreate()
        Log.d("MusicDetection", "Service created")
        sessionManager = getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager

        //creo il channel per le notifiche
        notificationUtils.createNotificationChannel(this,"Music Detection", "Clairaud's music detection service notification channel")

        // Avvia il servizio come foreground per mantenerlo attivo in background
        observeAutoEqState()
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("MusicDetection", "Listener connected")
        // avvia il polling delle sessioni media
    }
    private fun observeAutoEqState() {
        val applicationContext = this // per poter creare la notifica

        serviceScope.launch {
            AutoEqStateHolder.uiState.collectLatest { state ->
                if (state.isOn && !isPollingActive()) {
                    Log.d("MusicDetection", "Starting polling...")

                    //mando la notifica per segnalare il servizio on
                    val notification = notificationUtils.createNotification(
                        applicationContext,
                        false,
                        "Music detection",
                        "Detecting the song playing",
                        "Music detection service"
                    )
                    notificationUtils.sendNotification(applicationContext, notification, NOTIFICATION_ID)

                    checkForActiveSessions()
                } else if (!state.isOn && isPollingActive()) {
                    stopPolling()
                    Log.d("MusicDetection", "Stopping polling...")

                    //chiudo anche la notifica
                    notificationUtils.cancelNotification(applicationContext, NOTIFICATION_ID)
                }
            }
        }
    }

    private fun checkForActiveSessions() {
        pollingJob = serviceScope.launch {
            try {
                while (isActive) {
                    try {
                        val component = ComponentName(this@MusicDetectionService, javaClass)
                        val controllers = sessionManager.getActiveSessions(component)

                        var foundMusic = false
                        for (controller in controllers) {
                            val metadata = controller.metadata
                            if (metadata != null) {
                                val title = metadata.getString(MediaMetadata.METADATA_KEY_TITLE) ?: ""
                                val artist = metadata.getString(MediaMetadata.METADATA_KEY_ARTIST) ?:
                                metadata.getString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST) ?: ""

                                val artists = artist.split(",") //trovo tutti gli artisti coinvolti

                                if (title.isNotEmpty() && (title != lastTitle || artist != lastArtist)) {
                                    //se è diversa dalla traccia precedente, posso procedere con il call dell'api per capire il genere
                                    Log.d("MusicDetection", "Detected: $title by $artist")
                                    //chiamo la api
                                    var tags: List<Tag> = listOf()
                                    for(item in artists){ //visto che non posso sapere quale sia l'artista principale, provo con tutti finche' non trovo dei tag, oppure finisco gli artisti
                                        val artistName = item.trim()
                                        val response = LastFmApi.retrofitService.getTopTags(
                                            artist = artistName,
                                            track = title,
                                            apiKey = API_KEY
                                        )
                                        tags = response.toptags.tag
                                            .distinctBy { it.name.lowercase() }
                                        if(tags.isNotEmpty())
                                            break
                                    }


                                    tags.forEach {
                                        Log.d("LastFm", "Tag: ${it.name}")
                                    }
                                    Log.d("MusicDetection", "Response: $tags")

                                    lastTitle = title
                                    lastArtist = artist

                                    val preset = UserRepo.getPresetToApply(tags.toSet())

                                    if(preset.id != -1){ //cambio il preset solamente se ne trovo uno applicabile che non sia il default
                                        AutoEqStateHolder.changePreset(preset)
                                        EqRepo.newBands(preset.bands)
                                        Log.d("MusicDetection","Bands changed")
                                    }
                                    //cambio la notifica mettendo il testo per le canzoni rilevate
                                    val updateNotification = notificationUtils.createNotification(
                                        applicationContext,
                                        false,
                                        "Music detection",
                                        "Detected $title by $artist\n" +
                                                "Applied preset: ${if(preset.id == -1) "Not detected" else preset.name}",
                                        "Music detection service"
                                    )
                                    notificationUtils.sendNotification(applicationContext, updateNotification, NOTIFICATION_ID)
                                }

                                foundMusic = true
                                break
                            }
                        }

                        if (!foundMusic && lastTitle.isNotEmpty()) {
                            // nessuna musica attiva, resetta lo stato
                            lastTitle = ""
                            lastArtist = ""
                        }
                    } catch (e: Exception) {
                        Log.e("MusicDetection", "Error checking sessions", e)
                    }

                    // aspetta 5 secondi prima del prossimo controllo
                    Log.d("MusicDetection", "Waiting...")
                    Thread.sleep(5000)
                }
            }catch (e: CancellationException) { //serve altrimenti blocca l'eccezione e il job non termina
                Log.d("Polling", "Job cancelled")
                throw e
            }catch (e: Exception) {
                Log.e("Polling", "Error: ${e.message}")
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        stopPolling()
        serviceScope.cancel()
        Log.d("MusicDetection", "Service destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }
}