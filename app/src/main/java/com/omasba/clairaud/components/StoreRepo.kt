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
                author = "Mario Bava"
            ),
            EqPreset(
                name = "Rap",
                tags = mutableSetOf(
                    Tag(name = "rap"),
                    Tag(name = "hip-hop")),
                bands = arrayListOf(Pair<Int,Short>(60,4),
                Pair<Int,Short>(250,-2),
                Pair<Int,Short>(1000,1),
                Pair<Int,Short>(4000,3),
                Pair<Int,Short>(14000,4)),
                id = 11,
                author = "Francesco Rigatone"

            ),
            EqPreset(
                name = "Metal",
                tags = mutableSetOf(
                    Tag(name = "metal"),
                    Tag(name = "nu metal")),
                id = 12,
                author = "Geremia"

            ),
        )
        emit(samplePresets)
    }
}