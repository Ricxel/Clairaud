package com.omasba.clairaud.autoeq.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.omasba.clairaud.autoeq.MusicDetectionService
import com.omasba.clairaud.autoeq.state.AutoEqStateHolder
import com.omasba.clairaud.autoeq.state.AutoEqUiState
import com.omasba.clairaud.model.EqPresetModel
import kotlinx.coroutines.flow.StateFlow

class AutoEqViewModel : ViewModel() {
    val uiState: StateFlow<AutoEqUiState> = AutoEqStateHolder.uiState

    fun changeIsOn(value: Boolean) {
        AutoEqStateHolder.setIsOn(value)
        Log.d("autoeq", "isOn: ${uiState.value.isOn}")
    }

    fun toggleIsOn(){
        AutoEqStateHolder.setIsOn(!uiState.value.isOn)
    }
    fun changePreset(preset: EqPresetModel) {
        AutoEqStateHolder.changePreset(preset)
    }
}
