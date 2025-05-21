package com.omasba.clairaud.repos

import android.util.Log
import com.omasba.clairaud.repos.EqRepo.TAG
import com.omasba.clairaud.state.EqPreset
import com.omasba.clairaud.state.Tag
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.isActive
import kotlinx.coroutines.flow.update

object StoreRepo {
    //funzione per fare la query che ritorna il flow di preset
    private val _presets = MutableStateFlow(emptyList<EqPreset>())
    val presets = _presets.asStateFlow()

    fun addPreset(preset:EqPreset){
        _presets.update {
            it + preset
        }

        Log.d(TAG, "Preset added ~ ${preset.name}")
    }
    fun removePreset(preset:EqPreset){
        _presets.update {
            it - preset
        }

        Log.d(TAG, "Preset removed ~ ${preset.name}")
    }
    fun replacePreset(preset:EqPreset){

        _presets.update {
            it.filter{current ->
                current.id != preset.id
            }
        }
        this.addPreset(preset)

        Log.d(TAG, "Preset replaced ~ current -> ${preset.name}")
    }
    //restituisce i preset creati dall'utente
    fun getMyPresets(uid: Int):List<EqPreset>{
        val myPresets = _presets.value.filter { it.authorUid == uid }
        Log.d(TAG, "User presets created")

        return myPresets
    }
    suspend fun fetchPresets() {
        delay(1000)
        val samplePresets = listOf(
            EqPreset(
                name = "Rock",
                tags = mutableSetOf(
                    Tag(name = "rock"),
                    Tag(name = "alternative-rock")),
                bands = arrayListOf(
                    Pair<Int,Short>(0, 400),
                    Pair<Int,Short>(1, 200),
                    Pair<Int,Short>(2, 0),
                    Pair<Int,Short>(3, 300),
                    Pair<Int,Short>(4, 200)),
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
                    Pair<Int,Short>(0,400),
                    Pair<Int,Short>(1,-200),
                    Pair<Int,Short>(2,100),
                    Pair<Int,Short>(3,300),
                    Pair<Int,Short>(4,400)),
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
                    Pair<Int,Short>(0, 500),
                    Pair<Int,Short>(1, 100),
                    Pair<Int,Short>(2, -100),
                    Pair<Int,Short>(3, 400),
                    Pair<Int,Short>(4, 300)),
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
                    Pair<Int,Short>(0,200),
                    Pair<Int,Short>(1,300),
                    Pair<Int,Short>(2,400),
                    Pair<Int,Short>(3,200),
                    Pair<Int,Short>(4,100)),
                id = 4,
                author = "Marco Corradin"
            ),

            EqPreset(
                name = "Classical",
                tags = mutableSetOf(
                    Tag(name = "classical"),
                    Tag(name = "orchestral")),
                bands = arrayListOf(
                    Pair<Int,Short>(0,-100),
                    Pair<Int,Short>(1,0),
                    Pair<Int,Short>(2,0),
                    Pair<Int,Short>(3,100),
                    Pair<Int,Short>(4,200)),
                id = 5,
                author = "Mario Mazzaretto"
            ),

            EqPreset(
                name = "Electronic",
                tags = mutableSetOf(
                    Tag(name = "electronic"),
                    Tag(name = "edm"),
                    Tag(name = "dance")),
                bands = arrayListOf(
                    Pair<Int,Short>(0,500),
                    Pair<Int,Short>(1,300),
                    Pair<Int,Short>(2,100),
                    Pair<Int,Short>(3,400),
                    Pair<Int,Short>(4,500)),
                id = 6,
                author = "Simone Simoncelli"
            ),

            EqPreset(
                name = "Jazz",
                tags = mutableSetOf(
                    Tag(name = "jazz"),
                    Tag(name = "blues")),
                bands = arrayListOf(
                    Pair<Int,Short>(0,-200),
                    Pair<Int,Short>(1,0),
                    Pair<Int,Short>(2,200),
                    Pair<Int,Short>(3,300),
                    Pair<Int,Short>(4,100)),
                id = 7,
                author = "Michele Ballaben"
            ),

            EqPreset(
                name = "Metal",
                tags = mutableSetOf(
                    Tag(name = "metal"),
                    Tag(name = "heavy")),
                bands = arrayListOf(
                    Pair<Int,Short>(0,400),
                    Pair<Int,Short>(1,200),
                    Pair<Int,Short>(2,-100),
                    Pair<Int,Short>(3,300),
                    Pair<Int,Short>(4,400)),
                id = 8,
                author = "Nico Rapisarda"
            ),

            EqPreset(
                name = "Reggae",
                tags = mutableSetOf(
                    Tag(name = "reggae"),
                    Tag(name = "dub")),
                bands = arrayListOf(
                    Pair<Int,Short>(0,0),
                    Pair<Int,Short>(1,400),
                    Pair<Int,Short>(2,300),
                    Pair<Int,Short>(3,100),
                    Pair<Int,Short>(4,0)),
                id = 9,
                author = "Alfonso Fusolini"
            ),

            EqPreset(
                name = "Country",
                tags = mutableSetOf(
                    Tag(name = "country"),
                    Tag(name = "folk")),
                bands = arrayListOf(
                    Pair<Int,Short>(0,200),
                    Pair<Int,Short>(1,300),
                    Pair<Int,Short>(2,100),
                    Pair<Int,Short>(3,0),
                    Pair<Int,Short>(4,100)),
                id = 10,
                author = "Trevor Postelli"
            )
        )
        _presets.update {
            samplePresets
        }
    }
}