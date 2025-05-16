package com.omasba.clairaud.model

import java.lang.reflect.Constructor

data class EqPreset(
    var tags: Set<Tag> = emptySet(),
    var name: String = "DefaultPreset",
    var bands: ArrayList<Pair<Int,Short>> = arrayListOf( //default preset
        Pair(60, 0),
        Pair(250, 0),
        Pair(500, 0),
        Pair(2000, 0),
        Pair(6000, 0),
        Pair(8000, 0),
        Pair(16000, 0)
    ),
    var author: String = "",
    val id: Int = -1
)