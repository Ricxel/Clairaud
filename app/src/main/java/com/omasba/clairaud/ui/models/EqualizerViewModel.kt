package com.omasba.clairaud.ui.models

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Paint
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import com.omasba.clairaud.ui.components.EqualizerUiState
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EqualizerViewModel : ViewModel() {
    private val _eqState = MutableStateFlow(EqualizerUiState())
    val eqState = _eqState.asStateFlow()

    var isOn by mutableStateOf(false)
        private set

    var bands by mutableStateOf(ArrayList<Pair<Int, Short>>())
        private set

    init {
        this.NewBands(arrayListOf(
            Pair(60, 0),
            Pair(250, 0),
            Pair(500, 0),
            Pair(2000, 0),
            Pair(6000, 0),
            Pair(8000, 0),
            Pair(16000, 0)
        )
        )
    }

    fun ToggleEq(){
        isOn = !isOn


        _eqState.update{ currentState ->
            currentState.copy(isOn = this.isOn)
        }
    }

    fun NewBands(newBands: ArrayList<Pair<Int, Short>>) {
        bands = newBands

        _eqState.update { currentState ->
            currentState.copy(bands = newBands)
        }
    }

}