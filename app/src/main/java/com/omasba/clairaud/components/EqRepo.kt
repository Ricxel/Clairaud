package com.omasba.clairaud.components

import android.content.Context
import android.media.MediaPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object EqRepo 11{
    val media = MediaPlayer()

    private val eq = Eq(0)
    private val _eqState = MutableStateFlow(EqualizerUiState())
    val eqState = _eqState.asStateFlow()

    fun setIsOn(isOn:Boolean){
        //eq.isOn(isOn)

        _eqState.update{ currentState ->
            currentState.copy(isOn = isOn)
        }
    }

    fun newBands(newBands: ArrayList<Pair<Int, Short>>) {
        //eq.setAllBands(newBands)

        _eqState.update { currentState ->
            currentState.copy(bands = newBands)
        }
    }
}