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

    fun getBand(hz:Int):Int{
        return _eq.value?.getBand(hz) ?: -1
    }

    fun setBand(index:Int, level:Short){
        _eq.value?.setBandLevel(index, (level))

        _eqState.update { currentState ->
            currentState.copy(
                bands = _eq.value?.getAllBands() ?: arrayListOf( // se l'eq e' null, gli slider non si muovono
                        Pair<Int,Short>(1,0),
                        Pair<Int,Short>(2,0),
                        Pair<Int,Short>(3,0),
                        Pair<Int,Short>(4,0),
                        Pair<Int,Short>(5,0)
                    )
                )
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

            _eq.update {
                it?.setAllBands(newBands)
                it
            }

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