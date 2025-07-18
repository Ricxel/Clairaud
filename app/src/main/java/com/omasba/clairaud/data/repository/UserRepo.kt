package com.omasba.clairaud.data.repository

import com.omasba.clairaud.presentation.store.state.EqPreset
import com.omasba.clairaud.presentation.store.state.Tag
import com.omasba.clairaud.presentation.auth.state.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * User state holder repository
 */
object UserRepo {
    var currentUserProfile: UserProfile = UserProfile()
    private val _favPresets = MutableStateFlow<Set<Int>>(currentUserProfile!!.favPresets)
    val favPresets = _favPresets.asStateFlow()
    fun getPresetToApply(tags: Set<Tag>): EqPreset {
        //prendo i preset dallo store
        var maxCount = 0
        var correctPreset = EqPreset()

        _favPresets.value.forEach{ id ->
            run {
                val preset = StoreRepo.presets.value.find { it.id == id } ?: EqPreset()
                val tagsIntersection = preset.tags intersect tags
                if (tagsIntersection.count() > maxCount){
                    maxCount = tagsIntersection.count()
                    correctPreset = preset
                }
            }
        }
        return correctPreset
    }

    /**
     * @param id Preset ID
     * @return true if the specified preset is a favourite one by the current user
     */
    fun isFavorite(id:Int):Boolean{
        return _favPresets.value.contains(id);
    }

    /**
     * Add to user's favourites a preset
     * @param id Preset Id
     */
    fun addFavorite(id: Int){
//        val tmp = mutableSetOf<Int>()
//        tmp.addAll(_favPresets.value)
//        tmp.add(id)
//        _favPresets.value = tmp
        _favPresets.update { it + id }
    }

    /**
     * Remove a preset from the user's favourites
     * @param id Preset ID to remove
     */
    fun removeFavorite(id: Int){
//        val tmp = mutableSetOf<Int>()
//        tmp.addAll(_favPresets.value)
//        tmp.remove(id)
//        _favPresets.value = tmp
        _favPresets.update { it - id }
    }

    init {
    }
}