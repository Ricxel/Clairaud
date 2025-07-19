package com.omasba.clairaud.data.repository

import android.util.Log
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.omasba.clairaud.data.repository.EqRepo.TAG
import com.omasba.clairaud.presentation.store.state.EqPreset
import com.omasba.clairaud.presentation.store.state.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Presets store state holder repository
 */
object StoreRepo {
    //funzione per fare la query che ritorna il flow di preset
    private var _presets = MutableStateFlow(emptyList<EqPreset>())
    val presets = _presets.asStateFlow()

    //per segnalare che i preset sono caricati
    private val _presetsLoaded = MutableStateFlow(false)
    val presetsLoaded = _presetsLoaded.asStateFlow()

    private val presetsCollection = Firebase.firestore.collection("presets")

    fun empty(){
        _presets = MutableStateFlow(emptyList<EqPreset>())
    }

    fun reset(){
        _presets.update { emptyList() }
    }
    fun fetchPresets() {
        Log.d(TAG, "fetching")

        //svuoto i preset per triggerare l'animazione
        _presets.value = emptyList()
        _presetsLoaded.update { false }

        presetsCollection.get()
            .addOnSuccessListener { result ->
                val loadedPresets = result.documents.mapNotNull { doc ->
                    try {
                        val name = doc.getString("name") ?: return@mapNotNull null
                        val id = doc.getLong("id")?.toInt() ?: return@mapNotNull null
                        val author = doc.getString("author") ?: ""
                        val authorUid = doc.getString("authorUid") ?: ""
                        val tagsList = doc.get("tags") as? List<String> ?: emptyList()
                        val tags = tagsList.map { Tag(it) }.toSet()

                        val bandsList = doc.get("bands") as? List<Map<String, Any>> ?: emptyList()
                        val bands = bandsList.mapNotNull { map ->
                            val index = (map["index"] as? Long)?.toInt()
                            val gain = (map["gain"] as? Long)?.toShort()
                            if (index != null && gain != null) Pair(index, gain) else null
                        }

                        EqPreset(
                            name = name,
                            tags = tags,
                            bands = ArrayList(bands),
                            id = id,
                            author = author,
                            authorUid = authorUid
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Errore parsing preset: ${e.message}")
                        null
                    }
                }

                Log.d(TAG, "fetched$loadedPresets")

                _presets.value = loadedPresets
                _presetsLoaded.update { true }
            }
            .addOnFailureListener {
                Log.e(TAG, "Errore fetch preset: ${it.message}")
            }
    }

    /**
     * Remove a preset
     * @param preset Preset to be removed
     */
    fun removePreset(preset: EqPreset) {
        presetsCollection.document(preset.id.toString())
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "Preset deleted from Firestore: ${preset.name}")
            }
            .addOnFailureListener {
                Log.e(TAG, "Failed to delete preset: ${it.message}")
            }
        fetchPresets()
    }

    /**
     * Convert database model EqPreset to an HashMap
     * @param preset Preset to be converted
     * @return Preset convertend in HashMap<String,Any> format
     */
    fun presetMapFrom(preset: EqPreset):HashMap<String, Any>{
        val presetMap = hashMapOf(
            "name" to preset.name,
            "tags" to preset.tags.map { it.name }.toList(),
            "id" to preset.id,
            "author" to preset.author,
            "authorUid" to preset.authorUid.toString(),
            "bands" to preset.bands.map { mapOf("index" to it.first, "gain" to it.second.toInt()) } // <-- conversione
        )

        return presetMap
    }

    /**
     * Add a preset to firestore database
     * @param preset Preset to be added
     */
    fun addPreset(preset: EqPreset) {
        Log.d(TAG, "adding preset ${preset.name} to firebase5")

        presetsCollection.document(preset.id.toString())
            .set(presetMapFrom(preset), SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "Preset salvato su Firestore: ${preset.name}")
            }
            .addOnFailureListener {
                Log.e(TAG, "Errore salvataggio: ${it.message}")
            }

        Log.d(TAG, "preset added succesfully to firebase")
        fetchPresets()
    }

    /**
     * Replace a preset
     * @param preset New preset, it will replace the current one with the same id
     */
    fun replacePreset(preset: EqPreset) {
        addPreset(preset) // Firestore `set()` sovrascrive il documento
        fetchPresets()
    }
}