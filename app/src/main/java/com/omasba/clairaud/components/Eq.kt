package com.omasba.clairaud.components

import android.content.Context
import android.media.audiofx.AudioEffect
import android.media.audiofx.Equalizer

class Eq(private val sessionId: Int) {
    private var equalizer: Equalizer? = null

    init {
//        equalizer = Equalizer(0, 0).apply {
//            enabled = true
//        }
    }


    fun setBandLevel(band: Int, level: Short) {
        equalizer?.setBandLevel(band.toShort(), level)
    }

    fun getBandLevel(band: Int): Short {
        return equalizer?.getBandLevel(band.toShort()) ?: 0
    }

    fun getNumberOfBands(): Int {
        return equalizer?.numberOfBands?.toInt() ?: 0
    }

    fun getBandRange(): Pair<Short, Short> {
        val range = equalizer?.bandLevelRange
        return Pair(range?.get(0) ?: 0, range?.get(1) ?: 0)
    }

    fun setAllBands(bands: ArrayList<Pair<Int, Short>>) {
        bands.forEach {
            this.setBandLevel(it.first, it.second)
        }

    }

    fun isOn(isOn:Boolean){
        equalizer?.enabled = isOn
    }

    fun release() { // IMPORTANTE
        equalizer?.release()
    }
}
