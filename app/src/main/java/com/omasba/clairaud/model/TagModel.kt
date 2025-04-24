package com.omasba.clairaud.model

data class TagModel(
    val name: String,
    val count: Int = 0, //presente solo nei tag scaricati da lastfm
    val url: String = "" //presente solo nei tag scaricati da lastfm
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TagModel) return false
        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}