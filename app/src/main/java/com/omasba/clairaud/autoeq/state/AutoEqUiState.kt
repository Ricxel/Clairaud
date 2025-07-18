package com.omasba.clairaud.autoeq.state

import com.omasba.clairaud.state.EqPreset

/**
 * Class that describe the state of the AutoEq feature
 */
data class AutoEqUiState (
    val isOn: Boolean = false,
    val currentPreset: EqPreset = EqPreset(),
)