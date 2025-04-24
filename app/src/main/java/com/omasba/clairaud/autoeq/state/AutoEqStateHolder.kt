package com.omasba.clairaud.autoeq.state

import android.util.Log
import com.omasba.clairaud.autoeq.utils.AutoEqualizerUtils
import com.omasba.clairaud.model.EqPresetModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object AutoEqStateHolder {
    private val _uiState = MutableStateFlow(AutoEqUiState())
    val uiState = _uiState.asStateFlow()

    fun setIsOn(value: Boolean){
        _uiState.update { it.copy(isOn = value) }
    }
    fun changePreset(preset: EqPresetModel) {
        _uiState.update { it.copy(currentPreset = preset) }
    }
//    fun updateGenre(artist: String, title: String): String{
//        val tags = AutoEqualizerUtils.getTrackTags(artist = artist, track = title, AutoEqualizerUtils.API_KEY)
//        val genre = tags.firstOrNull() ?: "Not detected"
//        changeGenre(genre)
//
//        //ora che so i tag, bisogna intersecare quelli dei preset scaricati dall'utente con quelli trovati nella traccia
//        //TODO
//        Log.d("MusicDetection", "" + tags)
//
//        return genre
//    }
}