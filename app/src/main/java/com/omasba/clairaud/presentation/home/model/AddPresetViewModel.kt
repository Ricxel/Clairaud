package com.omasba.clairaud.presentation.home.model

import androidx.lifecycle.ViewModel
import com.omasba.clairaud.data.repository.StoreRepo
import com.omasba.clairaud.data.repository.UserRepo
import com.omasba.clairaud.presentation.store.state.EqPreset
import com.omasba.clairaud.presentation.store.state.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

val TAG = "AddPresetViewModel"

class AddPresetViewModel : ViewModel() {
    private val _eqPreset = MutableStateFlow(EqPreset(authorUid = "").apply { name = "" })
    val eqPreset = _eqPreset.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val TAG_MAX_DIM = 10

    fun changePreset(preset: EqPreset) {
        _eqPreset.value = preset
    }

    fun updatePresetName(name: String) {
        _eqPreset.update { current ->
            current.copy(name = name)
        }
    }

    fun addTag(tag: Tag) {
        val name = tag.name.trim() //sanitizzazione
        if (name.isBlank() || name.length > TAG_MAX_DIM){
            _error.value = "Tag is blank or too long"
            return
        }

        _eqPreset.update {
            it.copy(tags = it.tags + tag.copy(name = name))
        }
        _error.value = ""
    }

    fun removeTag(tag: Tag) {
        _eqPreset.update {
            it.copy(tags = it.tags - tag)
        }
    }

    //aggiunge o modifica il preset
    fun confirmPreset(bands: ArrayList<Pair<Int, Short>>): Boolean {
        //sanifico
        _eqPreset.update { it.copy(name = it.name.trim()) }
        if (_eqPreset.value.name.isBlank()) {
            _error.value = "The preset name is empty"
            return false
        }

        if (_eqPreset.value.authorUid != (UserRepo.currentUserProfile.uid)) {
            //vuol dire che è nuovo
            _eqPreset.update {
                it.copy(
                    authorUid = UserRepo.currentUserProfile.uid,
                    bands = bands,
                    author = UserRepo.currentUserProfile.username,
                    id = Random.nextInt(100, 100000)
                )
            }
            StoreRepo.addPreset(_eqPreset.value)
        } else {
            //vuol dire che lo sto modificando
            StoreRepo.replacePreset(preset = _eqPreset.value)
        }
        //resetta
        _error.value = null
        this.changePreset(EqPreset(name = "")) //nuovo preset pulito
        return true
    }
}