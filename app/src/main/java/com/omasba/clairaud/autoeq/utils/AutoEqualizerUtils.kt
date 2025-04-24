package com.omasba.clairaud.autoeq.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import com.google.gson.GsonBuilder
import com.omasba.clairaud.model.TagModel
import com.omasba.clairaud.ui.components.TrackTagsResponse
import com.omasba.clairaud.ui.components.TrackTagsResponseDeserializer
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder

class AutoEqualizerUtils {
    companion object {
        val API_KEY: String = "51b710eed01b56a851a342cca1bead2a"
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

        fun getTrackTags(artist: String, track: String, apiKey: String): Set<TagModel> {
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

                val tagSet =
                    json.tags //ottengo i tag direttamente in un set<String> perchè sto usando il sezializzatore custom
                Log.d("TrackTags", "$tagSet")

                var tagSetRet = mutableSetOf<TagModel>()
                tagSet.forEach{
                    tagSetRet.add(TagModel(name = it))
                }
                return tagSetRet
            }
        }
    }

}