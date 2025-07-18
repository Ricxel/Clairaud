package com.omasba.clairaud.autoeq.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.omasba.clairaud.autoeq.state.AutoEqStateHolder
import com.omasba.clairaud.autoeq.state.AutoEqUiState
import com.omasba.clairaud.presentation.store.state.EqPreset
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
    fun changePreset(preset: EqPreset) {
        AutoEqStateHolder.changePreset(preset)
    }
}
