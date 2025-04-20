package com.omasba.clairaud.ui.components

import android.util.Log
import androidx.compose.runtime.collectAsState
import com.omasba.clairaud.components.AutoEqualizer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object AutoEqRepo { //oggetto singleton
    var title: String = ""
    var artist: String = ""
    private var _isOn = MutableStateFlow(false);
    val isOn = _isOn.asStateFlow()
    private val _currentGenre = MutableStateFlow("")
    val currentGenre = _currentGenre.asStateFlow()

    //il genere viene aggiornato e viene fatta la chiamata alla api
    fun updateGenre(artist: String, title: String): String{
        if(!_isOn.value){
            _currentGenre.value = ""
            return ""
        }
        val tags = AutoEqualizer.getTrackTags(artist = artist, track = title, AutoEqualizer.API_KEY)
        val genre = tags.firstOrNull() ?: "Not detected"
        _currentGenre.value = genre

        //ora che so i tag, bisogna intersecare quelli dei preset scaricati dall'utente con quelli trovati nella traccia
        //TODO
        Log.d("MusicDetection", "" + tags)

        return genre
    }
    fun setIsOn(value: Boolean){
        _isOn.value = value
        if(value){
            CoroutineScope(Dispatchers.IO).launch { // devo farla qua perchè nel thread main non si posso fare chiamate ad api bloccanti
                val response = updateGenre(artist = artist, title = title)
                Log.d("SetIsOn", "Artista: $artist - $title - $response")
            }
        }
        else _currentGenre.value = ""
    }
}