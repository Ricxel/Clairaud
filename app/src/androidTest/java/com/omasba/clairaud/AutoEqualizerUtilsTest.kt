package com.omasba.clairaud

import android.util.Log
import com.omasba.clairaud.state.EqPreset
import com.omasba.clairaud.state.Tag
import com.omasba.clairaud.state.UserProfile
import org.junit.Assert.assertTrue
import org.junit.Test

class AutoEqualizerUtilsTest {
    @Test
    fun getIntersectionWithUserPresets(){
        val currentUserProfile = UserProfile(
            uid = "12",
            token = "alsdaksd",
            username = "Ziopedro",
            mail = "skibidi123@gmail.com"
        )
        currentUserProfile.presets.add(EqPreset(
            tags = mutableSetOf(Tag("rap"), Tag("hip hop"), Tag("hip-hop")),
            id = 1
        ))
        currentUserProfile.presets.add(
            EqPreset(
                tags = mutableSetOf(Tag("hip-hop")),
                id = 2
        ))
        currentUserProfile.favPresets.addAll(arrayOf(1,2))
        //tags ricevute dalla query
        val tags: Set<Tag> = setOf(Tag(name = "rap"), Tag(name = "hip-hop"))
        val presetToApply = currentUserProfile.getPresetToApply(tags)
        Log.d("preset", "id: " + presetToApply.id)
        assertTrue(presetToApply.id == 1)
    }
}