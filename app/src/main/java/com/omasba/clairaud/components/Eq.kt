package com.omasba.clairaud.components

import android.media.audiofx.Equalizer
import android.util.Log
import com.omasba.clairaud.repos.EqRepo

data class Eq(private val sessionId: Int, private val eq:Eq? = null) {
    private var equalizer: Equalizer? = null
    val TAG = "Eq"

    init{
        if(eq == null){
            equalizer = Equalizer(0, sessionId)
            Log.d("Skibidi", "${equalizer!!.getCenterFreq(1)}")
            Log.d("Skibidi", "Livello banda: ${equalizer!!.getBandLevel(1)}")
        }else{
            equalizer = Equalizer(0, sessionId)
            this.setAllBands(eq.getAllBands())
        }
        EqRepo.newBands(this.getAllBands())
//        EqRepo.newBands(StoreRepo.presets.value[1].bands)

        Log.d(TAG, "Equalizer init: ${this.properties().toString()}")
    }

    fun properties(): Equalizer.Settings? {
        return equalizer?.properties
    }

    //restitusce l'array di coppie (Hz,dB)
    fun getBandsFormatted(bands:ArrayList<Pair<Int,Short>>):ArrayList<Pair<Int,Short>>{
        val newBands = ArrayList<Pair<Int,Short>>()
        bands.forEach{band ->
            val freq = equalizer?.getCenterFreq(band.first.toShort()) ?: 0 //mi trovo la frequenza della banda
            newBands.add(Pair<Int,Short>(freq/1000,(band.second/100).toShort()))
        }
        return newBands

    }

    fun setBandLevel(band: Int, level: Short) {
        equalizer?.setBandLevel(band.toShort(), level)

    }

    fun getBandLevel(band: Int): Short {
        return equalizer?.getBandLevel(band.toShort()) ?: 0
    }
    fun getFreq(index: Short):Int{
        val freq = equalizer!!.getCenterFreq(index) /1000
        return freq
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
