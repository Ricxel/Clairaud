package com.omasba.clairaud.components

import android.media.audiofx.Equalizer
import android.util.Log
import com.omasba.clairaud.ui.components.EqualizerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object EqRepo{
    val TAG = "EqRepo"
    private val _eqState = MutableStateFlow(EqualizerUiState())
    val eqState = _eqState.asStateFlow()
    private val _eq = MutableStateFlow<Eq?>(null)
    val eq = _eq.asStateFlow()

    fun setEq(equalizer:Eq?){
        _eq.update { equalizer }
    }

    fun setBand(index:Int, level:Short, newBands: ArrayList<Pair<Int, Short>>){
        Log.d(TAG, "repoBands: ${newBands.toList()}")
        _eq.value?.setBandLevel(index, (level).toShort())

        _eqState.update { currentState ->
            currentState.copy(bands = newBands)
        }
    }
    fun changeBandLevel(freq: Int, newValue: Short){
        _eq.update { currentEq ->
            val newEq = currentEq?.copy()
            newEq?.setBandLevel(band = freq, level = newValue)
            newEq
        }
    }

    fun newBands(newBands: ArrayList<Pair<Int, Short>>) {
        try{
            Log.d(TAG, "bande: " + newBands.toList().toString())

            _eq.value?.setAllBands(newBands)

            _eqState.update { currentState ->
                currentState.copy(bands = newBands)
            }
        }catch (e:Exception){
            Log.d(TAG, e.message.toString())
        }

    }

    fun setIsOn(isOn:Boolean){
        _eq.value?.setIsOn(isOn)
        Log.d(TAG, "enabled: " + isOn.toString())


        _eqState.update{ currentState ->
            currentState.copy(isOn = isOn)
        }
    }
}