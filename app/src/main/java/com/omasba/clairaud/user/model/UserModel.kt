package com.omasba.clairaud.user.model

import androidx.compose.runtime.mutableStateListOf
import com.omasba.clairaud.model.EqPresetModel
import com.omasba.clairaud.model.TagModel
import com.omasba.clairaud.user.UserRepo.currentUser

class UserModel (
    var presets: ArrayList<EqPresetModel> = arrayListOf(),
    var favPresets: ArrayList<Int> = arrayListOf(), // lista degli id dei preset preferitit
    var uid: String,
    var token: String,
    var username: String,
    var mail: String
){
    fun getPresetToApply(tags: Set<TagModel>): EqPresetModel{
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