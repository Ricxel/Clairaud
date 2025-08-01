package com.omasba.clairaud.data.repository

import com.omasba.clairaud.presentation.home.state.AutoEqUiState
import com.omasba.clairaud.presentation.store.state.EqPreset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Repository for AutoEq feature state
 */
object AutoEqRepo {
    private val _uiState = MutableStateFlow(AutoEqUiState())
    val uiState = _uiState.asStateFlow()

    fun setIsOn(value: Boolean) {
        _uiState.update { it.copy(isOn = value) }
    }

    /**
     * Change the active preset
     * @param preset Preset to apply
     */
    fun changePreset(preset: EqPreset) {
        _uiState.update { it.copy(currentPreset = preset) }
    }
}