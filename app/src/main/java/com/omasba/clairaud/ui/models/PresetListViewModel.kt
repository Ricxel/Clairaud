package com.omasba.clairaud.ui.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasba.clairaud.components.StoreRepo
import com.omasba.clairaud.model.EqPreset
import com.omasba.clairaud.model.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PresetListViewModel:ViewModel() {
    private val _presets = MutableStateFlow<List<EqPreset>>(emptyList()) // andra popolata con la query a firebase
    val presets = _presets.asStateFlow()

    //per filtare con i tag
    private val _selectedTags = MutableStateFlow<Set<Tag>>(emptySet())
    val selectedTags = _selectedTags.asStateFlow()

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

    val filteredItemsByTags = combine(filteredPresets, selectedTags) { items, tags ->
        if (tags.isEmpty()) items
        else items.filter { it.tags.any { tag -> tag in tags } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun onQueryChanged(newQuery: String){
        _query.value = newQuery
    }
    fun onTagSelected(tag: Tag) {
        _selectedTags.update { it + tag }
    }

    fun onTagRemoved(tag: Tag) {
        _selectedTags.update { it - tag }
    }
    init {
        viewModelScope.launch {
            StoreRepo.getPresets().collect{ presetList ->
                _presets.value = presetList
            }
        }
    }

}