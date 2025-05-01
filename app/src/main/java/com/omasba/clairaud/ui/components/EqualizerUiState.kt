package com.omasba.clairaud.ui.components

data class EqualizerUiState (
    val isOn: Boolean = false,
    val bands: ArrayList<Pair<Int, Short>> = ArrayList<Pair<Int, Short>>()
)