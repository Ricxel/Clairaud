package com.omasba.clairaud.components

import com.omasba.clairaud.model.EqPreset
import com.omasba.clairaud.model.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking

object StoreRepo {
    //funzione per fare la query che ritorna il flow di preset
    private val _presets = MutableStateFlow(emptyList<EqPreset>())
    val presets = _presets.asStateFlow()
    init {
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
                bands = arrayListOf(
                    Pair<Int,Short>(60,4),
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
            EqPreset(
                name = "Pop",
                tags = mutableSetOf(
                    Tag(name = "pop"),
                    Tag(name = "mainstream")),
                bands = arrayListOf(
                    Pair<Int,Short>(1,2),
                    Pair<Int,Short>(2,3),
                    Pair<Int,Short>(3,4),
                    Pair<Int,Short>(4,2),
                    Pair<Int,Short>(5,1)),
                id = 4,
                author = "Francesco Rigatone"
            ),

            EqPreset(
                name = "Classical",
                tags = mutableSetOf(
                    Tag(name = "classical"),
                    Tag(name = "orchestral")),
                bands = arrayListOf(
                    Pair<Int,Short>(1,-1),
                    Pair<Int,Short>(2,0),
                    Pair<Int,Short>(3,0),
                    Pair<Int,Short>(4,1),
                    Pair<Int,Short>(5,2)),
                id = 5,
                author = "Francesco Rigatone"
            ),

            EqPreset(
                name = "Electronic",
                tags = mutableSetOf(
                    Tag(name = "electronic"),
                    Tag(name = "edm"),
                    Tag(name = "dance")),
                bands = arrayListOf(
                    Pair<Int,Short>(1,5),
                    Pair<Int,Short>(2,3),
                    Pair<Int,Short>(3,1),
                    Pair<Int,Short>(4,4),
                    Pair<Int,Short>(5,5)),
                id = 6,
                author = "Francesco Rigatone"
            ),

            EqPreset(
                name = "Jazz",
                tags = mutableSetOf(
                    Tag(name = "jazz"),
                    Tag(name = "blues")),
                bands = arrayListOf(
                    Pair<Int,Short>(1,-2),
                    Pair<Int,Short>(2,0),
                    Pair<Int,Short>(3,2),
                    Pair<Int,Short>(4,3),
                    Pair<Int,Short>(5,1)),
                id = 7,
                author = "Francesco Rigatone"
            ),

            EqPreset(
                name = "Metal",
                tags = mutableSetOf(
                    Tag(name = "metal"),
                    Tag(name = "heavy")),
                bands = arrayListOf(
                    Pair<Int,Short>(1,4),
                    Pair<Int,Short>(2,2),
                    Pair<Int,Short>(3,-1),
                    Pair<Int,Short>(4,3),
                    Pair<Int,Short>(5,4)),
                id = 8,
                author = "Francesco Rigatone"
            ),

            EqPreset(
                name = "Reggae",
                tags = mutableSetOf(
                    Tag(name = "reggae"),
                    Tag(name = "dub")),
                bands = arrayListOf(
                    Pair<Int,Short>(1,0),
                    Pair<Int,Short>(2,4),
                    Pair<Int,Short>(3,3),
                    Pair<Int,Short>(4,1),
                    Pair<Int,Short>(5,0)),
                id = 9,
                author = "Francesco Rigatone"
            ),

            EqPreset(
                name = "Country",
                tags = mutableSetOf(
                    Tag(name = "country"),
                    Tag(name = "folk")),
                bands = arrayListOf(
                    Pair<Int,Short>(1,2),
                    Pair<Int,Short>(2,3),
                    Pair<Int,Short>(3,1),
                    Pair<Int,Short>(4,0),
                    Pair<Int,Short>(5,1)),
                id = 10,
                author = "Francesco Rigatone"
            )
        )
        _presets.update {
            samplePresets
        }
    }
    fun addPreset(preset:EqPreset){
        _presets.update {
            it + preset
        }
    }
}