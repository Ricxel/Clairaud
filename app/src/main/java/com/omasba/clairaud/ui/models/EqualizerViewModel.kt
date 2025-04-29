package com.omasba.clairaud.ui.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.omasba.clairaud.components.EqRepo

class EqualizerViewModel : ViewModel() {
    val eqState = EqRepo.eqState

    var isOn by mutableStateOf(false)
        private set

    init {
        this.newBands(arrayListOf(
            Pair(60, 0),
            Pair(250, 0),
            Pair(500, 0),
            Pair(2000, 10),
            Pair(6000, 0),
            Pair(8000, 0),
            Pair(16000, 0)
        )
        )
    }

    fun newBands(newBands: ArrayList<Pair<Int, Short>>){
        EqRepo.newBands(newBands)
    }

    fun toggleEq(){
        EqRepo.setIsOn(!eqState.value.isOn)
    }



}