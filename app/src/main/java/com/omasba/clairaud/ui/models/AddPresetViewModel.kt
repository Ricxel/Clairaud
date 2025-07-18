package com.omasba.clairaud.ui.models

import androidx.lifecycle.ViewModel
import com.omasba.clairaud.repos.StoreRepo
import com.omasba.clairaud.repos.UserRepo
import com.omasba.clairaud.state.EqPreset
import com.omasba.clairaud.state.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

val TAG = "AddPresetViewModel"

class AddPresetViewModel:ViewModel(){
    private val _eqPreset = MutableStateFlow(EqPreset(authorUid = "").apply { name = "" })
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
    //aggiunge o modifica il preset
    fun confirmPreset(bands: ArrayList<Pair<Int,Short>>):Boolean{
        if(_eqPreset.value.name.isBlank()){
            _showError.value = true
            return false
        }
        if(_eqPreset.value.authorUid != (UserRepo.currentUserProfile?.uid ?: "")){
            //vuol dire che è nuovo
            _eqPreset.update {
                it.copy(
                    authorUid = UserRepo.currentUserProfile!!.uid,
                    bands = bands,
                    author = UserRepo.currentUserProfile!!.username,
                    id = Random.nextInt(100,100000)
                )
            }
            StoreRepo.addPreset(_eqPreset.value)
        }
        else{
            //vuol dire che lo sto modificando
            StoreRepo.replacePreset(preset = _eqPreset.value)
        }
        //resetta
        _showError.value = false
        this.changePreset(EqPreset(name = "")) //nuovo preset pulito
        return true
    }
}