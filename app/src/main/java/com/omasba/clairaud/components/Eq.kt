package com.omasba.clairaud.components

import android.media.audiofx.Equalizer

class Eq(private val sessionId: Int) {
    private var equalizer: Equalizer? = null

    init {
        equalizer = Equalizer(0, sessionId).apply {
            enabled = true
        }
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

    fun setAllBands(levels: ShortArray) {
        val bands = getNumberOfBands()
        if (levels.size != bands) {
            throw IllegalArgumentException(".")
        }

        for (i in levels.indices) {
            setBandLevel(i, levels[i])
        }
    }

    fun disable() {
        equalizer?.enabled = false
    }

    fun enable() {
        equalizer?.enabled = true
    }

    fun release() { // IMPORTANTE
        equalizer?.release()
    }
}
