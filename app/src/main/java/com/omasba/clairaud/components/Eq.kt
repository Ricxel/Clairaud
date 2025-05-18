package com.omasba.clairaud.components

import android.media.audiofx.Equalizer
import android.util.Log

data class Eq(private val sessionId: Int, private val eq:Eq? = null) {
    private var equalizer: Equalizer? = null
    val TAG = "Eq"

    init{
        if(eq == null){
            equalizer = Equalizer(0, sessionId)
        }else{
            equalizer = Equalizer(0, sessionId)
            this.setAllBands(eq.getAllBands())
        }

        Log.d(TAG, "Equalizer init: ${this.properties().toString()}")
    }

    fun properties(): Equalizer.Settings? {
        return equalizer?.properties
    }

    fun setBandLevel(band: Int, level: Short) {
        equalizer?.setBandLevel(band.toShort(), level)

    }

    fun getBandLevel(band: Int): Short {
        return equalizer?.getBandLevel(band.toShort()) ?: 0
    }

    fun getBand(hz:Int):Int?{
        return equalizer?.getBand(hz * 1000)?.toInt()
    }

    fun getNumberOfBands(): Int {
        return equalizer?.numberOfBands?.toInt() ?: 0
    }

    fun getBandRange(): Pair<Short, Short> {
        val range = equalizer?.bandLevelRange
        return Pair(range?.get(0) ?: 0, range?.get(1) ?: 0)
    }

    fun getAllBands():ArrayList<Pair<Int, Short>>{
        val nBands = this.getNumberOfBands()
        val result:ArrayList<Pair<Int, Short>> = ArrayList<Pair<Int, Short>>()
        Log.d(TAG, "nBands: " + nBands)

        for(band in 0..nBands-1){
            result.add(Pair(band, this.getBandLevel(band)))
        }

        Log.d(TAG, "result: " + result.toString())

        return result
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
