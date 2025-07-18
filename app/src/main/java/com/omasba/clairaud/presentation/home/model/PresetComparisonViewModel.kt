package com.omasba.clairaud.presentation.home.model

import androidx.lifecycle.ViewModel
import com.omasba.clairaud.presentation.store.state.EqPreset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PresetComparisonViewModel : ViewModel() {
    private val _selectedPreset = MutableStateFlow<EqPreset?>(null)
    val selectedPreset = _selectedPreset.asStateFlow()

    fun selectPreset(preset: EqPreset) {
        _selectedPreset.value = preset
    }
}