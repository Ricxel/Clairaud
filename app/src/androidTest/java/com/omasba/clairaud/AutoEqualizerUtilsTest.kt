package com.omasba.clairaud

import android.util.Log
import com.omasba.clairaud.autoeq.utils.AutoEqualizerUtils
import org.junit.Assert.assertTrue
import org.junit.Test

class AutoEqualizerUtilsTest {

    @Test
    fun getTrackTags_returnsExpectedTags() {
        val artist = "Radiohead"
        val track = "Creep"
        val apiKey = "51b710eed01b56a851a342cca1bead2a"

        val tags = AutoEqualizerUtils.getTrackTags(artist, track, apiKey)
        Log.d("TrackTagsTest","Tag: '$track' di '$artist': $tags")

        assertTrue("rock" in tags || "alternative" in tags)
    }
}