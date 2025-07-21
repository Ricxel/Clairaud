package com.omasba.clairaud.presentation.store.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasba.clairaud.data.repository.StoreRepo
import com.omasba.clairaud.data.repository.UserRepo
import com.omasba.clairaud.presentation.store.state.EqPreset
import com.omasba.clairaud.presentation.store.state.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class StoreViewModel : ViewModel() {
    val presets = StoreRepo.presets
    val TAG = "StoreViewModel"

    //per vedere se i preset sono caricati
    val presetsLoaded = StoreRepo.presetsLoaded

    //per filtare con i tag
    private val _selectedTags = MutableStateFlow<Set<Tag>>(emptySet())
    val selectedTags = _selectedTags.asStateFlow()

    //per la funzione di ricerca
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    //per gestire i preferiti
    val favPresets = UserRepo.favPresets

    private val _filterByFavorites = MutableStateFlow(false)
    val filterByFavorites = _filterByFavorites.asStateFlow()

    private val _showUserPresets = MutableStateFlow(false)
    val showUserPresets = _showUserPresets.asStateFlow()

    //filtro sui preferiti
    val favoritePresets: StateFlow<List<EqPreset>> = combine(
        presets,
        favPresets
    ) { allPresets, favIds ->
        allPresets.filter { preset -> preset.id in favIds }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    //filtro unificato
    val filteredPresets: StateFlow<List<EqPreset>> = combine(
        presets,
        favPresets,
        selectedTags,
        _query,
        _showUserPresets,
        _filterByFavorites
    ) { values ->
        //obbligatorio il cast perchè con 6 parametri non c'è type inference
        val allPresets: List<EqPreset> = values[0] as List<EqPreset>
        val favIds: Set<Int> = values[1] as Set<Int>
        val tags: Set<Tag> = values[2] as Set<Tag>
        val query: String = values[3] as String
        val filterUser: Boolean = values[4] as Boolean
        val filterFav: Boolean = values[5] as Boolean


        val currentUserUid = UserRepo.currentUserProfile.uid

        allPresets.filter { preset ->
            (!filterFav || preset.id in favIds) &&
                    (query.isBlank() ||
                            preset.name.contains(query, ignoreCase = true) ||
                            preset.name.contains(query, ignoreCase = true) == true) &&
                    (!filterUser || preset.authorUid == currentUserUid) &&
                    (tags.isEmpty() || tags.any { it in preset.tags })
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }

    fun toggleFavoriteFilter() {
        _filterByFavorites.update { !it }
    }

    fun toggleShowUserPresets() {
        _showUserPresets.update { !it }
    }

    fun onTagSelected(tag: Tag) {
        _selectedTags.update { it + tag }
    }

    fun fetchPresets() {
        StoreRepo.fetchPresets()
    }

    fun onTagRemoved(tag: Tag) {
        _selectedTags.update { it - tag }
    }
}