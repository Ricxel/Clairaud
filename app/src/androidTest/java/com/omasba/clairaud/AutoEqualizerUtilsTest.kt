package com.omasba.clairaud

import android.util.Log
import com.omasba.clairaud.autoeq.utils.AutoEqualizerUtils
import com.omasba.clairaud.model.EqPresetModel
import com.omasba.clairaud.model.TagModel
import com.omasba.clairaud.user.model.UserModel
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
    @Test
    fun getIntersectionWithUserPresets(){
        val currentUser = UserModel(
            uid = "12",
            token = "alsdaksd",
            username = "Ziopedro",
            mail = "skibidi123@gmail.com"
        )
        currentUser.presets.add(EqPresetModel(
            tags = mutableSetOf(TagModel("rap"), TagModel("hip hop"), TagModel("hip-hop")),
            id = 1
        ))
        currentUser.presets.add(
            EqPresetModel(
                tags = mutableSetOf(TagModel("hip-hop")),
                id = 2
        ))
        currentUser.favPresets.addAll(arrayOf(1,2))
        //tags ricevute dalla query
        val tags: Set<TagModel> = setOf(TagModel(name = "rap"), TagModel(name = "hip-hop"))
        val presetToApply = currentUser.getPresetToApply(tags)
        Log.d("preset", "id: " + presetToApply.id)
        assertTrue(presetToApply.id == 1)
    }
}