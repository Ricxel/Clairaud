package com.omasba.clairaud.presentation.home.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.omasba.clairaud.data.repository.EqRepo

class EqualizerViewModel : ViewModel() {
    val TAG: String = "EqViewModel"
    val eqState = EqRepo.eqState


    /**
     * Sets the level of a specific equalizer band
     * @param index The index (0 to 4) of the band to modify
     * @param level The new level for the band
     */
    fun setBand(index: Int, level: Short) {
        Log.d(TAG, "Setting band $index to $level")
        EqRepo.setBand(index, level)
    }

    /**
     * Sets a new set of bands
     * @param newBands The list of new bands
     */
    fun newBands(newBands: ArrayList<Pair<Int, Short>>) {
        EqRepo.newBands(newBands)
    }

    /**
     * Toggles the equalizer on or off
     */
    fun toggleEq() {
        Log.d(TAG, "Equalizer toggled")
        EqRepo.setIsOn(!eqState.value.isOn)
    }
}