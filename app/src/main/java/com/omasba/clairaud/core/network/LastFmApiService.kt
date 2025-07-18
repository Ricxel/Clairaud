package com.omasba.clairaud.core.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://ws.audioscrobbler.com"
const val API_KEY: String = "51b710eed01b56a851a342cca1bead2a"

private val json = Json{ ignoreUnknownKeys = true}
private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface LastFmApiService{
    @GET("2.0/")
    suspend fun getTopTags(
        @Query("method") method: String = "track.gettoptags",
        @Query("artist") artist: String,
        @Query("track") track: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json"
    ): TopTagsResponse
}
object LastFmApi {
    val retrofitService: LastFmApiService by lazy {
        retrofit.create(LastFmApiService::class.java)
    }
}