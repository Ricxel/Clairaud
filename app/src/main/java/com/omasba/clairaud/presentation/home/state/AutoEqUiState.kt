package com.omasba.clairaud.presentation.home.state

import com.omasba.clairaud.presentation.store.state.EqPreset

/**
 * Class that describe the state of the AutoEq feature
 */
data class AutoEqUiState (
    val isOn: Boolean = false,
    val currentPreset: EqPreset = EqPreset(),
)