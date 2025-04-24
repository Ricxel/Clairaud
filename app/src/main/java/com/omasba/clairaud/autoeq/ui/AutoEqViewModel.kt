package com.omasba.clairaud.autoeq.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.omasba.clairaud.autoeq.MusicDetectionService
import com.omasba.clairaud.autoeq.state.AutoEqStateHolder
import com.omasba.clairaud.autoeq.state.AutoEqUiState
import kotlinx.coroutines.flow.StateFlow

class AutoEqViewModel : ViewModel() {
    val uiState: StateFlow<AutoEqUiState> = AutoEqStateHolder.uiState

    fun changeIsOn(value: Boolean) {
        AutoEqStateHolder.setIsOn(value)
        if(!value)
            MusicDetectionService.stopPolling()
        Log.d("autoeq", "isOn: ${uiState.value.isOn}")
    }

    fun toggleIsOn(){
        AutoEqStateHolder.setIsOn(!uiState.value.isOn)
        if(!uiState.value.isOn)
            MusicDetectionService.stopPolling()
    }
    fun changeGenre(newGenre: String) {
        AutoEqStateHolder.changeGenre(newGenre)
    }
}
