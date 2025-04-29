package com.omasba.clairaud.components

data class EqualizerUiState (
    val isOn: Boolean = false,
    val bands: ArrayList<Pair<Int, Short>> = ArrayList<Pair<Int, Short>>()
)