package com.omasba.clairaud.ui.models

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasba.clairaud.components.StoreRepo
import com.omasba.clairaud.model.EqPreset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.stateIn

class PresetListViewModel:ViewModel() {
    private val _presets = MutableStateFlow<List<EqPreset>>(emptyList()) // andra popolata con la query a firebase
    val presets = _presets.asStateFlow()

    //query per la funzione di ricerca
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    //funzione per filtare i preset dalla seach bar
    val filteredPresets: StateFlow<List<EqPreset>> = _query
        .map { query ->
            val items = _presets.value
            if(query.isBlank()) items
            else items.filter { it.name.contains(query, ignoreCase = true) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onQueryChanged(newQuery: String){
        _query.value = newQuery
    }
    init {
        viewModelScope.launch {
            StoreRepo.getPresets().collect{ presetList ->
                _presets.value = presetList
            }
        }
    }

}