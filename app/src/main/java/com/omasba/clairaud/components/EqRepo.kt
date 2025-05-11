package com.omasba.clairaud.components

import android.util.Log
import com.omasba.clairaud.ui.components.EqualizerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object EqRepo{
    val TAG = "EqRepo"
    private val _eqState = MutableStateFlow(EqualizerUiState())
    val eqState = _eqState.asStateFlow()
    var eqService: Eq? = null

    fun setBand(index:Int, level:Short, newBands: ArrayList<Pair<Int, Short>>){
        Log.d(TAG, "repoBands: ${newBands.toList()}")
        eqService?.setBandLevel(index, (level).toShort())

        _eqState.update { currentState ->
            currentState.copy(bands = newBands)
        }
    }

    fun newBands(newBands: ArrayList<Pair<Int, Short>>) {
        try{
            Log.d(TAG, "bande: " + newBands.toList().toString())

            eqService?.setAllBands(newBands)

            _eqState.update { currentState ->
                currentState.copy(bands = newBands)
            }
        }catch (e:Exception){
            Log.d(TAG, e.message.toString())
        }

    }

    fun setIsOn(isOn:Boolean){
        eqService?.setIsOn(isOn)
        Log.d(TAG, "enabled: " + isOn.toString())


        _eqState.update{ currentState ->
            currentState.copy(isOn = isOn)
        }
    }
}