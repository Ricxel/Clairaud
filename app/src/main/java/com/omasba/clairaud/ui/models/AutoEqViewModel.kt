package com.omasba.clairaud.ui.models

import androidx.lifecycle.ViewModel
import com.omasba.clairaud.ui.components.AutoEqRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AutoEqViewModel:ViewModel() {
    val detectedGenre: StateFlow<String> = AutoEqRepo.currentGenre
    val isOn: StateFlow<Boolean> = AutoEqRepo.isOn
    fun toggleIsOn(newValue: Boolean){
        AutoEqRepo.setIsOn(newValue)
    }
}