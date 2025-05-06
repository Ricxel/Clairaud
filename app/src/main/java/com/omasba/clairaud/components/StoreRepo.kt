package com.omasba.clairaud.components

import com.omasba.clairaud.model.EqPreset
import com.omasba.clairaud.model.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object StoreRepo {
    //funzione per fare la query che ritorla il flow di preset
    fun getPresets(): Flow<List<EqPreset>> = flow{
        val samplePresets = listOf(
            EqPreset(
                name = "Rock",
                tags = mutableSetOf(
                    Tag(name = "rock"),
                    Tag(name = "alternative-rock")),
                id = 10,
            ),
            EqPreset(
                name = "Rap",
                tags = mutableSetOf(
                    Tag(name = "rap"),
                    Tag(name = "hip-hop")),
                id = 11,
            ),
            EqPreset(
                name = "Metal",
                tags = mutableSetOf(
                    Tag(name = "metal"),
                    Tag(name = "nu metal")),
                id = 12,
            ),
        )
        emit(samplePresets)
    }
}