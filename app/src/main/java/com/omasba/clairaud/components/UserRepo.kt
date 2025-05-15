package com.omasba.clairaud.components

import com.omasba.clairaud.model.EqPreset
import com.omasba.clairaud.model.Tag
import com.omasba.clairaud.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserRepo {
    var currentUser: User = User(
        uid = "101",
        token = "skibidi",
        username = "LucaBandolero",
        mail = "Luca.Bandolero@ziocan.com"
    )
    private val _favPresets = MutableStateFlow<Set<Int>>(currentUser.favPresets)
    val favPreset = _favPresets.asStateFlow()
    fun getPresetToApply(tags: Set<Tag>): EqPreset{
        var maxCount = 0
        var correctPreset = EqPreset()
        _favPresets.value.forEach{ id ->
            run {
                val preset = StoreRepo.collectPresets().find { it.id == id } ?: EqPreset()
                val tagsIntersection = preset.tags intersect tags
                if (tagsIntersection.count() > maxCount){
                    maxCount = tagsIntersection.count()
                    correctPreset = preset
                }
            }
        }
        return correctPreset
    }
    fun isFavorite(id:Int):Boolean{
        return _favPresets.value.contains(id);
    }
    fun addFavorite(id: Int){
        val tmp = mutableSetOf<Int>()
        tmp.addAll(_favPresets.value)
        tmp.add(id)
        _favPresets.value = tmp
    }
    fun removeFavorite(id: Int){
        val tmp = mutableSetOf<Int>()
        tmp.addAll(_favPresets.value)
        tmp.remove(id)
        _favPresets.value = tmp
    }
    init {
    }
}