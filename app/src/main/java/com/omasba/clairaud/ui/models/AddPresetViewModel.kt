package com.omasba.clairaud.ui.models

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.omasba.clairaud.components.StoreRepo
import com.omasba.clairaud.model.EqPreset
import com.omasba.clairaud.model.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddPresetViewModel:ViewModel(){
    private val _eqPreset = MutableStateFlow(EqPreset().apply { name = "" })
    val eqPreset = _eqPreset.asStateFlow()

    fun changePreset(preset: EqPreset){
        _eqPreset.value = preset
    }
    fun updatePresetName(name:String){
        _eqPreset.update { current ->
            current.copy(name = name)
        }
    }
    fun addTag(tag: Tag){
        _eqPreset.update {
            it.copy(tags = it.tags + tag)
        }
    }

    fun removeTag(tag: Tag){
        _eqPreset.update {
            it.copy(tags = it.tags + tag)
        }
    }
    fun addPreset(){
        StoreRepo.addPreset(_eqPreset.value)
    }
}