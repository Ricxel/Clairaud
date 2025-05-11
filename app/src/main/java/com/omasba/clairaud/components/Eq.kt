package com.omasba.clairaud.components

import android.content.Context
import android.media.audiofx.AudioEffect
import android.media.audiofx.Equalizer
import android.util.Log

class Eq(private val sessionId: Int) {
    private var equalizer: Equalizer? = null
    val TAG = "Eq"

    init{
        equalizer = Equalizer(0, sessionId)
    }

    fun properties(): Equalizer.Settings? {
        return equalizer?.properties
    }

    fun setBandLevel(band: Int, level: Short) {
        //Log.d(TAG, "1: " + equalizer?.properties.toString())
        equalizer?.setBandLevel(band.toShort(), level)
        //Log.d(TAG, "2: " + equalizer?.properties.toString())

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

        try {
            bands.forEach {
                this.setBandLevel(it.first, it.second)
            }
        }catch (e:Exception){
            Log.d(TAG, e.message.toString() + bands.toList())
        }


        Log.d(TAG, "set bands for ${bands.toList().toString()}")
        Log.d(TAG, equalizer?.properties.toString())
    }

    fun setIsOn(isOn:Boolean){
        Log.d(TAG, "isOn: $isOn")
        equalizer?.enabled = isOn
    }


    fun release() { // IMPORTANTE
        Log.d(TAG, "releasing")
        equalizer?.release()
    }
}
