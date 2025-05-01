package com.omasba.clairaud.model

class User (
    var presets: ArrayList<EqPreset> = arrayListOf(),
    var favPresets: ArrayList<Int> = arrayListOf(), // lista degli id dei preset preferitit
    var uid: String,
    var token: String,
    var username: String,
    var mail: String
){
    fun getPresetToApply(tags: Set<Tag>): EqPreset{
        var maxCount = 0
        var correctPreset = EqPreset()
        favPresets.forEach{ id ->
            run {
                val preset = presets.find { it.id == id } ?: EqPreset()
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