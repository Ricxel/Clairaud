package com.omasba.clairaud.service.eq

import android.media.audiofx.Equalizer
import android.util.Log
import com.omasba.clairaud.data.repository.EqRepo

data class Eq(private val sessionId: Int, private val eq: Eq? = null) {
    private var equalizer: Equalizer? = null
    val TAG = "Eq"

    val priority = 1000
    init {
        if (eq == null) {
            equalizer = Equalizer(priority, sessionId)
            //imposta le bande a 0
            val bands = arrayListOf(
                Pair<Int, Short>(0, 0),
                Pair<Int, Short>(1, 0),
                Pair<Int, Short>(2, 0),
                Pair<Int, Short>(3, 0),
                Pair<Int, Short>(4, 0)
            )
            this.setAllBands(bands)
        } else {
            equalizer = Equalizer(priority, sessionId)
            this.setAllBands(eq.getAllBands())
        }
        EqRepo.newBands(this.getAllBands()) //li imposta anche nella UI

        Log.d(TAG, "Equalizer init: ${this.properties().toString()}")
    }

    /**
     * @return the current Equalizer settings
     */
    fun properties(): Equalizer.Settings? {
        return equalizer?.properties
    }

    //restitusce l'array di coppie (Hz,dB)
    fun getBandsFormatted(bands: ArrayList<Pair<Int, Short>>): ArrayList<Pair<Int, Short>> {
        val newBands = ArrayList<Pair<Int, Short>>()

        bands.forEach { band ->
            val freq = equalizer?.getCenterFreq(band.first.toShort())
                ?: 0 //mi trovo la frequenza della banda
            newBands.add(Pair<Int, Short>(freq / 1000, (band.second / 100).toShort()))
        }

        return newBands
    }

    /**
     * Sets the level of a specific equalizer band
     * @param band The index (0 to 4) of the band to modify
     * @param level The new level for the band
     */
    fun setBandLevel(band: Int, level: Short) {
        equalizer?.setBandLevel(band.toShort(), level)
        Log.d(TAG, "band $band set to $level")
    }

    /**
     * Retrieves a given band level
     * @param band the band index
     * @return the level of the band
     */
    fun getBandLevel(band: Int): Short {
        return equalizer?.getBandLevel(band.toShort()) ?: 0
    }

    /**
     * @return the level of a band given it index
     */
    fun getFreq(index: Short): Int {
        val freq = equalizer!!.getCenterFreq(index) / 1000
        return freq
    }

    /**
     * @return the number of bands of the device
     */
    fun getNumberOfBands(): Int {
        return equalizer?.numberOfBands?.toInt() ?: 0
    }

    /**
     * @return each frequency range of every band
     */
    fun getBandRange(): Pair<Short, Short> {
        val range = equalizer?.bandLevelRange
        return Pair(range?.get(0) ?: 0, range?.get(1) ?: 0)
    }

    /**
     * @return all bands paired with each band level
     */
    fun getAllBands(): ArrayList<Pair<Int, Short>> {
        val nBands = this.getNumberOfBands()
        val result: ArrayList<Pair<Int, Short>> = ArrayList<Pair<Int, Short>>()

        for (band in 0..<nBands) {
            result.add(Pair(band, this.getBandLevel(band)))
        }

        return result
    }

    /**
     * Sets all bands to new given levels
     * @param bands the new level of bands
     */
    fun setAllBands(bands: ArrayList<Pair<Int, Short>>) {

        try {
            bands.forEach {
                this.setBandLevel(it.first, it.second)
            }
            Log.d(TAG, "Setted bands: ${bands.toList()}")
        } catch (e: Exception) {
            Log.d(TAG, "Error setting bands: " + e.message.toString() + bands.toList())
        }

        Log.d(TAG, equalizer?.properties.toString())
    }

    /**
     * Toggles the Equalizer on or off
     */
    fun setIsOn(isOn: Boolean) {
        Log.d(TAG, "Equalizer enabled: $isOn")
        equalizer?.enabled = isOn
    }

    /**
     * Releases the memory allocated from the equalizer
     */
    fun release() { // IMPORTANTE
        Log.d(TAG, "releasing equalizer")
        equalizer?.release()
    }

    fun hasControl():Boolean{
        return equalizer!!.hasControl()
    }
}
