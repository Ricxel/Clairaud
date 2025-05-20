package com.omasba.clairaud.state

data class EqPreset(
    var tags: Set<Tag> = emptySet(),
    var name: String = "DefaultPreset",
    var bands: ArrayList<Pair<Int,Short>> = arrayListOf( //default preset
        Pair(0, 0),
        Pair(1, 0),
        Pair(2, 0),
        Pair(3, 0),
        Pair(4, 0),
    ),
    var author: String = "",
    var authorUid: Int = -1,
    val id: Int = -1
)