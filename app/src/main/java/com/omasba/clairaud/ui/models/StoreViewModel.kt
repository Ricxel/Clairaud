package com.omasba.clairaud.ui.models

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.composable
import com.omasba.clairaud.components.StoreRepo
import com.omasba.clairaud.components.UserRepo
import com.omasba.clairaud.model.EqPreset
import com.omasba.clairaud.model.Tag
import com.omasba.clairaud.ui.components.BottomNavItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoreViewModel:ViewModel() {
    val presets = StoreRepo.presets

    //per filtare con i tag
    private val _selectedTags = MutableStateFlow<Set<Tag>>(emptySet())
    val selectedTags = _selectedTags.asStateFlow()

    //query per la funzione di ricerca
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    //per gestire i preferiti
    val favPresets = UserRepo.favPresets

    //flag per filtro sui preferiti
    private val _filterByFavorites = MutableStateFlow(false)
    val filterByFavorites = _filterByFavorites.asStateFlow()

    //filtra per preferiti
    private val filteredPresetsByFav: StateFlow<List<EqPreset>> = _filterByFavorites
        .map { filter ->
            val items = presets.value
            if(!filter) {
                items
            }
            else items.filter { favPresets.value.contains(it.id) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    //funzione per filtare i preset dalla search bar
    private val filteredPresetsByQuery: StateFlow<List<EqPreset>> =
        combine(_query, filteredPresetsByFav) { query, items ->
            if (query.isBlank()) items
            else items.filter { it.name.contains(query, ignoreCase = true) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    //filtra per i tag
    val filteredItemsByTags = combine(filteredPresetsByQuery, selectedTags) { items, tags ->
        if (tags.isEmpty()) items
        else items.filter { it.tags.any { tag -> tag in tags } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun onQueryChanged(newQuery: String){
        _query.value = newQuery
    }
    fun toggleFavoriteFilter(){
        _filterByFavorites.value = !_filterByFavorites.value
    }
    fun onTagSelected(tag: Tag) {
        _selectedTags.update { it + tag }
    }

    fun onTagRemoved(tag: Tag) {
        _selectedTags.update { it - tag }
    }

}