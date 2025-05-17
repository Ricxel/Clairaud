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
        _presets.update {
            samplePresets
        }
    }
}