package com.omasba.clairaud.model

class EqPresetModel(
    var tags: MutableSet<Tag> = mutableSetOf(),
    var name: String = "DefaultPreset",
    var setting: ArrayList<Pair<Int,Short>> = arrayListOf( //default preset
        Pair(60, 0),
        Pair(250, 0),
        Pair(500, 0),
        Pair(2000, 0),
        Pair(6000, 0),
        Pair(8000, 0),
        Pair(16000, 0)
    ),
    val id: Int = -1
) {
    fun addTag(tag: String){
        tags.add(Tag(name = tag))
    }
}