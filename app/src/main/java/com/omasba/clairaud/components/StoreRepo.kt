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
        delay(1000)
        val samplePresets = listOf(
            EqPreset(
                name = "Rock",
                tags = mutableSetOf(
                    Tag(name = "rock"),
                    Tag(name = "alternative-rock")),
                id = 1,
                author = "Mario Bava",
                authorUid = 0
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
                author = "Francesco Rigatone",
                authorUid = 2


            ),
            EqPreset(
                name = "Metal",
                tags = mutableSetOf(
                    Tag(name = "metal"),
                    Tag(name = "nu metal")),
                id = 3,
                author = "Geremia",
                authorUid = 56
            ),
        )
        _presets.update {
            samplePresets
        }
    }
}