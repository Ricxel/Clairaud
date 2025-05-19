package com.omasba.clairaud.components

import androidx.lifecycle.viewModelScope
import com.omasba.clairaud.model.EqPreset
import com.omasba.clairaud.model.Tag
import com.omasba.clairaud.model.User
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object StoreRepo {
    //funzione per fare la query che ritorna il flow di preset
    private val _presets = MutableStateFlow(emptyList<EqPreset>())
    val presets = _presets.asStateFlow()

    fun addPreset(preset:EqPreset){
        _presets.update {
            it + preset
        }
    }
    //restituisce i preset creati dall'utente
    fun getMyPresets(uid: Int):List<EqPreset>{
        val myPresets = _presets.value.filter { it.authorUid == uid }
        return myPresets
    }
    suspend fun fetchPresets() {
        delay(200)
        val samplePresets = listOf(
            EqPreset(
                name = "Rock",
                tags = mutableSetOf(
                    Tag(name = "rock"),
                    Tag(name = "alternative-rock")),
                bands = arrayListOf(
                    Pair<Int,Short>(1, 400),
                    Pair<Int,Short>(2, 200),
                    Pair<Int,Short>(3, 0),
                    Pair<Int,Short>(4, 300),
                    Pair<Int,Short>(5, 200)),
                id = 1,
                author = "Mario Bava",
                authorUid = 0
            ),
            EqPreset(
                name = "Rap",
                tags = mutableSetOf(
                    Tag(name = "rap"),
                    Tag(name = "hip-hop")),
                bands = arrayListOf(
                    Pair<Int,Short>(1,400),
                    Pair<Int,Short>(1,-200),
                    Pair<Int,Short>(1,100),
                    Pair<Int,Short>(1,300),
                    Pair<Int,Short>(1,400)),
                id = 2,
                author = "Francesco Rigatone",
                authorUid = 2


            ),
            EqPreset(
                name = "Metal",
                tags = mutableSetOf(
                    Tag(name = "metal"),
                    Tag(name = "nu metal")),
                bands = arrayListOf(
                    Pair<Int,Short>(1, 500),
                    Pair<Int,Short>(2, 100),
                    Pair<Int,Short>(3, -100),
                    Pair<Int,Short>(4, 400),
                    Pair<Int,Short>(5, 300)),
                id = 3,
                author = "Geremia",
                authorUid = 56
            ),
            EqPreset(
                name = "Pop",
                tags = mutableSetOf(
                    Tag(name = "pop"),
                    Tag(name = "mainstream")),
                bands = arrayListOf(
                    Pair<Int,Short>(1,200),
                    Pair<Int,Short>(2,300),
                    Pair<Int,Short>(3,400),
                    Pair<Int,Short>(4,200),
                    Pair<Int,Short>(5,100)),
                id = 4,
                author = "Francesco Rigatone"
            ),

            EqPreset(
                name = "Classical",
                tags = mutableSetOf(
                    Tag(name = "classical"),
                    Tag(name = "orchestral")),
                bands = arrayListOf(
                    Pair<Int,Short>(1,-100),
                    Pair<Int,Short>(2,0),
                    Pair<Int,Short>(3,0),
                    Pair<Int,Short>(4,100),
                    Pair<Int,Short>(5,200)),
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
                    Pair<Int,Short>(1,500),
                    Pair<Int,Short>(2,300),
                    Pair<Int,Short>(3,100),
                    Pair<Int,Short>(4,400),
                    Pair<Int,Short>(5,500)),
                id = 6,
                author = "Francesco Rigatone"
            ),

            EqPreset(
                name = "Jazz",
                tags = mutableSetOf(
                    Tag(name = "jazz"),
                    Tag(name = "blues")),
                bands = arrayListOf(
                    Pair<Int,Short>(1,-200),
                    Pair<Int,Short>(2,0),
                    Pair<Int,Short>(3,200),
                    Pair<Int,Short>(4,300),
                    Pair<Int,Short>(5,100)),
                id = 7,
                author = "Francesco Rigatone"
            ),

            EqPreset(
                name = "Metal",
                tags = mutableSetOf(
                    Tag(name = "metal"),
                    Tag(name = "heavy")),
                bands = arrayListOf(
                    Pair<Int,Short>(1,400),
                    Pair<Int,Short>(2,200),
                    Pair<Int,Short>(3,-100),
                    Pair<Int,Short>(4,300),
                    Pair<Int,Short>(5,400)),
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
                    Pair<Int,Short>(2,400),
                    Pair<Int,Short>(3,300),
                    Pair<Int,Short>(4,100),
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
                    Pair<Int,Short>(1,200),
                    Pair<Int,Short>(2,300),
                    Pair<Int,Short>(3,100),
                    Pair<Int,Short>(4,0),
                    Pair<Int,Short>(5,100)),
                id = 10,
                author = "Francesco Rigatone"
            )
        )
        _presets.update {
            samplePresets
        }
    }
}