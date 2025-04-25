package com.omasba.clairaud

import android.util.Log
import com.omasba.clairaud.autoeq.utils.AutoEqualizerUtils
import com.omasba.clairaud.model.EqPresetModel
import com.omasba.clairaud.model.Tag
import com.omasba.clairaud.user.model.UserModel
import org.junit.Assert.assertTrue
import org.junit.Test

class AutoEqualizerUtilsTest {
    @Test
    fun getIntersectionWithUserPresets(){
        val currentUser = UserModel(
            uid = "12",
            token = "alsdaksd",
            username = "Ziopedro",
            mail = "skibidi123@gmail.com"
        )
        currentUser.presets.add(EqPresetModel(
            tags = mutableSetOf(Tag("rap"), Tag("hip hop"), Tag("hip-hop")),
            id = 1
        ))
        currentUser.presets.add(
            EqPresetModel(
                tags = mutableSetOf(Tag("hip-hop")),
                id = 2
        ))
        currentUser.favPresets.addAll(arrayOf(1,2))
        //tags ricevute dalla query
        val tags: Set<Tag> = setOf(Tag(name = "rap"), Tag(name = "hip-hop"))
        val presetToApply = currentUser.getPresetToApply(tags)
        Log.d("preset", "id: " + presetToApply.id)
        assertTrue(presetToApply.id == 1)
    }
}