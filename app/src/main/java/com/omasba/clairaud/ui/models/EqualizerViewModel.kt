package com.omasba.clairaud.ui.models

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.omasba.clairaud.components.EqRepo

class EqualizerViewModel : ViewModel() {
    val TAG:String = "EqViewModel"
    val eqState = EqRepo.eqState

    var isOn by mutableStateOf(false)
        private set

    init {
        this.newBands(arrayListOf(
            Pair(0, 0),
            Pair(1, 3),
            Pair(2, 0),
            Pair(3, 0),
            Pair(4, 0),
            Pair(5, 0),
            Pair(6, 0)
        )
        )
    }

    fun newBands(newBands: ArrayList<Pair<Int, Short>>){
        EqRepo.newBands(newBands)
        Log.d(TAG, "new bands: $newBands")
    }

    fun toggleEq(){
        EqRepo.setIsOn(!eqState.value.isOn)
    }



}