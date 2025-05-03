package com.omasba.clairaud.components

import android.app.Application
import android.content.Intent
import android.util.Log
import com.omasba.clairaud.ui.components.EqualizerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object EqRepo{
    val TAG = "EqService"
    private val _eqState = MutableStateFlow(EqualizerUiState())
    val eqState = _eqState.asStateFlow()
    var eqService = EqService().equalizer

    fun setIsOn(isOn:Boolean){
        eqService?.setEnabled(isOn)
        Log.d(TAG, "isOn: " + isOn.toString())


        _eqState.update{ currentState ->
            currentState.copy(isOn = isOn)
        }
    }

    fun newBands(newBands: ArrayList<Pair<Int, Short>>) {
        try{
            Log.d(TAG, "bande: " + newBands.toList().toString())

            for (band in newBands)
                eqService?.setBandLevel(band.first.toShort(), (band.second * 100).toShort())

            _eqState.update { currentState ->
                currentState.copy(bands = newBands)
            }
        }catch (e:Exception){
            Log.d(TAG, e.message.toString())
        }

    }
}