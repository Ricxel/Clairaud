package com.omasba.clairaud.components

import com.omasba.clairaud.model.EqPreset
import com.omasba.clairaud.model.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

object StoreRepo {
    //funzione per fare la query che ritorna il flow di preset
    fun getPresets(): Flow<List<EqPreset>> = flow{
        val samplePresets = listOf(
            EqPreset(
                name = "Rock",
                tags = mutableSetOf(
                    Tag(name = "rock"),
                    Tag(name = "alternative-rock")),
                id = 1,
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
                id = 2,
                author = "Francesco Rigatone"

            ),
            EqPreset(
                name = "Metal",
                tags = mutableSetOf(
                    Tag(name = "metal"),
                    Tag(name = "nu metal")),
                id = 3,
                author = "Geremia"

            ),
        )
        emit(samplePresets)
    }
    fun collectPresets(): List<EqPreset>{
        var presets: List<EqPreset> = emptyList()
        runBlocking {
            presets = getPresets().first()  // Raccoglie il primo valore emesso dal Flow
        }

        return presets
    }
}