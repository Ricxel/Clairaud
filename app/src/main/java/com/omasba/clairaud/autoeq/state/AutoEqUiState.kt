package com.omasba.clairaud.autoeq.state

import com.omasba.clairaud.state.EqPreset

data class AutoEqUiState (
    val isOn: Boolean = false,
    val currentPreset: EqPreset = EqPreset(),
)