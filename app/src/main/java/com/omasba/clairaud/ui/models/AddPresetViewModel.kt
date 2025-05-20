package com.omasba.clairaud.ui.models

import androidx.lifecycle.ViewModel
import com.omasba.clairaud.repos.StoreRepo
import com.omasba.clairaud.repos.UserRepo
import com.omasba.clairaud.state.EqPreset
import com.omasba.clairaud.state.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddPresetViewModel:ViewModel(){
    private val _eqPreset = MutableStateFlow(EqPreset(authorUid = -1).apply { name = "" })
    val eqPreset = _eqPreset.asStateFlow()

    private val _showError = MutableStateFlow(false)
    val showError = _showError.asStateFlow()

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
            it.copy(tags = it.tags - tag)
        }
    }
    //aggiunge il preset
    fun addPreset(bands: ArrayList<Pair<Int,Short>>):Boolean{
        if(_eqPreset.value.name.isBlank()){
            _showError.value = true
            return false
        }
        _eqPreset.update {
            it.copy(authorUid = UserRepo.currentUser.uid, bands = bands, author = UserRepo.currentUser.username)
        }
        StoreRepo.addPreset(_eqPreset.value)
        //resetta
        _showError.value = false
        this.changePreset(EqPreset(name = "")) //nuovo preset pulito
        return true
    }
}