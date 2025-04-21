package com.omasba.clairaud

import android.util.Log
import com.omasba.clairaud.ui.components.Eq
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class EqTest{

    @Test
    fun setAllBands(){
        val equalizer = Eq(0) // session id = 0 per tutto l'audio in uscita

        val range = equalizer.getBandRange()
        Log.i("range: ", range.toString())

        val bands = equalizer.getNumberOfBands()
        Log.i("bands: ", bands.toString())

        // Non ancora testato
        // val midLevel = ((range.first + range.second) / 2).toShort()
        // val levels = ShortArray(bands) { midLevel }
        // equalizer.setAllBands(levels)
    }
}