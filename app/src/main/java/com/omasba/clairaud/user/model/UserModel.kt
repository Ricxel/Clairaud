package com.omasba.clairaud.user.model

import com.omasba.clairaud.model.EqPresetModel
import com.omasba.clairaud.model.Tag

class UserModel (
    var presets: ArrayList<EqPresetModel> = arrayListOf(),
    var favPresets: ArrayList<Int> = arrayListOf(), // lista degli id dei preset preferiti
    var uid: String,
    var token: String,
    var username: String,
    var mail: String
){
    fun getPresetToApply(tags: Set<Tag>): EqPresetModel{
        var maxCount = 0
        var correctPreset = EqPresetModel()
        favPresets.forEach{ id ->
            run {
                val preset = presets.find { it.id == id } ?: EqPresetModel()
                val tagsIntersection = preset.tags intersect tags
                if (tagsIntersection.count() > maxCount){
                    maxCount = tagsIntersection.count()
                    correctPreset = preset
                }
            }
        }
        return correctPreset
    }
}