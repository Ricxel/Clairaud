package com.omasba.clairaud.autoeq.state

import com.omasba.clairaud.state.EqPreset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object AutoEqStateHolder {
    private val _uiState = MutableStateFlow(AutoEqUiState())
    val uiState = _uiState.asStateFlow()

    fun setIsOn(value: Boolean){
        _uiState.update { it.copy(isOn = value) }
    }
    fun changePreset(preset: EqPreset) {
        _uiState.update { it.copy(currentPreset = preset) }
    }
    fun reload(){
        _uiState.update { it.copy(isOn = false) }
        _uiState.update { it.copy(isOn = true) }
    }
}