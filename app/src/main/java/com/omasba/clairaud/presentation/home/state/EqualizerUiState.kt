package com.omasba.clairaud.presentation.home.state

data class EqualizerUiState (
    val isOn: Boolean = false,
    val bands: ArrayList<Pair<Int, Short>> = arrayListOf()
)