package com.omasba.clairaud.presentation.home.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.omasba.clairaud.data.repository.EqRepo

class EqualizerViewModel : ViewModel() {
    val TAG:String = "EqViewModel"
    val eqState = EqRepo.eqState

//    var isOn by mutableStateOf(false)
//        private set

    init {
        val bands = arrayListOf(
            Pair<Int,Short>(0,0),
            Pair<Int,Short>(1,0),
            Pair<Int,Short>(2,0),
            Pair<Int,Short>(3,0),
            Pair<Int,Short>(4,0))
        newBands(bands)
    }
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