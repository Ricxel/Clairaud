package com.omasba.clairaud.autoeq.state

import com.omasba.clairaud.model.EqPreset

data class AutoEqUiState (
    val isOn: Boolean = false,
    val currentPreset: EqPreset = EqPreset(),
)