package com.omasba.clairaud.ui.models

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.omasba.clairaud.repos.EqRepo

class EqualizerViewModel : ViewModel() {
    val TAG:String = "EqViewModel"
    val eqState = EqRepo.eqState

//    var isOn by mutableStateOf(false)
//        private set

    fun setBandByHz(hz:Int, level:Short){
        EqRepo.setBand(EqRepo.getBand(hz*1000), level)
    }
    fun getFreqByIndex(index: Short):Int{
        return EqRepo.getFreq(index)
    }
    fun setBand(index:Int, level:Short){
        Log.d(TAG, "Setting band $index to $level")
        EqRepo.setBand(index, level)
    }
    fun updateBandLevel(freq: Int, newValue: Short){
        EqRepo.changeBandLevel(freq, newValue)
    }

    fun newBands(newBands: ArrayList<Pair<Int, Short>>){
        EqRepo.newBands(newBands)
    }

    fun toggleEq(){
        Log.d(TAG, "Equalizer toggled")
        EqRepo.setIsOn(!eqState.value.isOn)
    }
}