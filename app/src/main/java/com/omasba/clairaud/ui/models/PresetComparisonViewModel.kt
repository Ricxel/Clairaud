package com.omasba.clairaud.ui.models

import androidx.lifecycle.ViewModel
import com.omasba.clairaud.model.EqPreset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PresetComparisonViewModel : ViewModel() {
    private val _selectedPreset = MutableStateFlow<EqPreset?>(null)
    val selectedPreset = _selectedPreset.asStateFlow()

    fun selectPreset(preset: EqPreset) {
        _selectedPreset.value = preset
    }
}