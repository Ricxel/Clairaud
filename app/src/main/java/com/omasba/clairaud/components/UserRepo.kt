package com.omasba.clairaud.components

import com.omasba.clairaud.model.EqPreset
import com.omasba.clairaud.model.Tag
import com.omasba.clairaud.model.User

object UserRepo {
    var currentUser: User? = User(
        uid = "101",
        token = "skibidi",
        username = "LucaBandolero",
        mail = "Luca.Bandolero@ziocan.com"
    )
    fun addPreset(preset: EqPreset){
        currentUser?.presets?.add(preset)
    }
    //ritorna il preset da applicare in base ai tag della canzone
    fun getPresetToApply(tags: Set<Tag>): EqPreset{
        return currentUser?.getPresetToApply(tags) ?: EqPreset()
    }
    init {
        addPreset(EqPreset(
            name = "Preset jazz rap",
            tags = mutableSetOf(Tag(name = "jazz"), Tag(name = "rap"), Tag(name = "hip-hop")),
            id = 1
        ))
        addPreset(EqPreset(
            name = "Preset kenny",
            tags = mutableSetOf(Tag(name = "hip-hop"), Tag(name = "spoken word"), Tag(name = "neo-soul")),
            id = 2
        ))
        currentUser?.favPresets?.addAll(arrayOf(1,2))
    }
}