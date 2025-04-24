package com.omasba.clairaud.user

import com.omasba.clairaud.model.EqPresetModel
import com.omasba.clairaud.model.TagModel
import com.omasba.clairaud.user.model.UserModel

object UserRepo {
    var currentUser: UserModel? = UserModel(
        uid = "101",
        token = "skibidi",
        username = "LucaBandolero",
        mail = "Luca.Bandolero@ziocan.com"
    )
    fun addPreset(preset: EqPresetModel){
        currentUser?.presets?.add(preset)
    }
    //ritorna il preset da applicare in base ai tag della canzone
    fun getPresetToApply(tags: Set<TagModel>): EqPresetModel{
        return currentUser?.getPresetToApply(tags) ?: EqPresetModel()
    }
    init {
        addPreset(EqPresetModel(
            name = "Preset jazz rap",
            tags = mutableSetOf(TagModel(name = "jazz"), TagModel(name = "rap"), TagModel(name = "hip-hop")),
            id = 1
        ))
        addPreset(EqPresetModel(
            name = "Preset kenny",
            tags = mutableSetOf(TagModel(name = "hip-hop"), TagModel(name = "spoken word"), TagModel(name = "neo-soul")),
            id = 2
        ))
        currentUser?.favPresets?.addAll(arrayOf(1,2))
    }
}