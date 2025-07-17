package com.omasba.clairaud.ui.models

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasba.clairaud.repos.StoreRepo
import com.omasba.clairaud.repos.UserRepo
import com.omasba.clairaud.state.EqPreset
import com.omasba.clairaud.state.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class StoreViewModel:ViewModel() {
    val presets = StoreRepo.presets
    val TAG = "StoreViewModel"

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

       //flag per filtro sui preset dell'utente
    private val _showUserPresets = MutableStateFlow(false)
    val showUserPresets = _showUserPresets.asStateFlow()

    val favoritePresetsOnly: StateFlow<List<EqPreset>> = combine(presets, favPresets) { allPresets, favoritePresetIds ->
        allPresets.filter { favoritePresetIds.contains(it.id) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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


    //per mostrare solo quelli dell'utente
    private val userPresets: StateFlow<List<EqPreset>> =
        combine(filteredPresetsByQuery, _showUserPresets) { items, filter ->
            if (!filter) items
            else items.filter { it.authorUid == UserRepo.currentUser.uid }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    //filtra per i tag
    val filteredItemsByTags = combine(userPresets, selectedTags) { items, tags ->
        if (tags.isEmpty()) items
        else items.filter { it.tags.any { tag -> tag in tags } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    fun onQueryChanged(newQuery: String){
        _query.value = newQuery
    }
    fun toggleFavoriteFilter(){
        _filterByFavorites.update { !it }
    }
    fun toggleShowUserPresets(){
        _showUserPresets.update { !it }
    }
    fun onTagSelected(tag: Tag) {
        _selectedTags.update { it + tag }
    }

    fun fetchPresets(){
        StoreRepo.fetchPresets()
    }

    fun fetchFavPresets():List<EqPreset>{
        return filteredPresetsByFav.value
    }

    fun empty(){
        StoreRepo.empty()
    }

    fun onTagRemoved(tag: Tag) {
        _selectedTags.update { it - tag }
    }

}