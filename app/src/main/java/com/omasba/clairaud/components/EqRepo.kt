package com.omasba.clairaud.components

import android.media.MediaPlayer
import com.omasba.clairaud.ui.components.EqualizerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object EqRepo{
    val media = MediaPlayer()
    private val eq = Eq(media.audioSessionId)

    private val _eqState = MutableStateFlow(EqualizerUiState())
    val eqState = _eqState.asStateFlow()

    fun setIsOn(isOn:Boolean){
        eq.isOn(isOn)

        _eqState.update{ currentState ->
            currentState.copy(isOn = isOn)
        }
    }

    fun newBands(newBands: ArrayList<Pair<Int, Short>>) {
        eq.setAllBands(newBands)

        _eqState.update { currentState ->
            currentState.copy(bands = newBands)
        }
    }
}