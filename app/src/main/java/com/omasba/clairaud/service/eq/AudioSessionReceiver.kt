package com.omasba.clairaud.service.eq

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.audiofx.AudioEffect
import android.media.audiofx.Equalizer
import android.util.Log
import com.omasba.clairaud.data.repository.EqRepo

/**
 * Receiver class that allows to receive music players signals
 * @param equalizer Active equalizer
 */
class AudioSessionReceiver : BroadcastReceiver() {
    private var equalizer: Eq? = null
    private val TAG = "EqService"
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Receive")

        if (intent == null) return
        val sessionId = intent.getIntExtra(AudioEffect.EXTRA_AUDIO_SESSION, AudioEffect.ERROR)

        if (sessionId == AudioEffect.ERROR) return


        when (intent.action) {
            AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION -> {
                Log.d(TAG, "Audio session opened: $sessionId")

                try {
                    if(EqRepo.eq.value == null){
                        equalizer = Eq(sessionId)
                        EqRepo.setEq(equalizer)

                        Log.d(TAG, "Built a new equalizer")
                    }else{
                        equalizer = Eq(sessionId, EqRepo.eq.value)
                        EqRepo.setEq(equalizer)

                        Log.d(TAG, "Cloned last equalizer")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Equalizer error: ${e.message}")
                }
            }

            AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION -> {
                Log.d(TAG, "Audio session closed: $sessionId")
            }
        }
    }

    /**
     * Release the eq, needed when the service is destroyed
     */
    fun releaseEq(){
        equalizer?.setIsOn(false)
        equalizer?.release()
        Log.d(TAG, "Destroy")
    }
}