package com.omasba.clairaud.data.repository

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.omasba.clairaud.presentation.store.state.EqPreset
import com.omasba.clairaud.presentation.store.state.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.lang.Thread.sleep

/**
 * Presets store state holder repository
 */
object StoreRepo {
    const val TAG = "store"

    // ritorna il flow di preset
    private var _presets = MutableStateFlow(emptyList<EqPreset>())
    val presets = _presets.asStateFlow()

    // segnala che i preset sono caricati (caricamento dello store)
    private val _presetsLoaded = MutableStateFlow(false)
    val presetsLoaded = _presetsLoaded.asStateFlow()

    private val presetsCollection = Firebase.firestore.collection("presets")

    /**
     * Clears all the presets in the state flow
     */
    fun reset(){
        // per poter rifare la query a firebase
        _presets.update { emptyList() }
        Log.d(TAG, "Store reset done")

    }

    /**
     * Fetches all presets from Firebase and includes the author name.
     * Updates the presets StateFlow with the result.
     */
    fun fetchPresets() {
        Log.d(TAG, "fetching")

        // svuoto i preset per triggerare l'animazione
        _presets.value = emptyList()
        _presetsLoaded.update { false }

        presetsCollection.get()
            .addOnSuccessListener { result ->
                val presetDocs = result.documents

                // Task per fare 2 query a diverse tabelle di firebase ed unire i risultati
                val tasks = presetDocs.mapNotNull { doc ->
                    val name = doc.getString("name") ?: return@mapNotNull null
                    val id = doc.getLong("id")?.toInt() ?: return@mapNotNull null
                    val authorUid = doc.getString("authorUid") ?: return@mapNotNull null

                    if (authorUid.isEmpty()) {
                        Log.e(TAG, "Preset ${doc.id} has a null or invalid AuthorUid")
                        return@mapNotNull null
                    }


                    val tagsList = doc.get("tags") as? List<String> ?: emptyList()
                    val tags = tagsList.map { Tag(it) }.toSet()

                    val bandsList = doc.get("bands") as? List<Map<String, Any>> ?: emptyList()
                    val bands = bandsList.mapNotNull { map ->
                        val index = (map["index"] as? Long)?.toInt()
                        val gain = (map["gain"] as? Long)?.toShort()
                        if (index != null && gain != null) Pair(index, gain) else null
                    }

                    // ottengo il nome autore da users
                    Firebase.firestore.collection("users").document(authorUid).get()
                        .continueWith { task ->
                            val userDoc = task.result
                            val authorName = userDoc?.getString("username") ?: "Sconosciuto"

                            EqPreset(
                                name = name,
                                tags = tags,
                                bands = ArrayList(bands),
                                id = id,
                                author = authorName,
                                authorUid = authorUid
                            )
                        }
                }

                // tutte chiamate sono completate
                Tasks.whenAllSuccess<EqPreset>(tasks)
                    .addOnSuccessListener { loadedPresets ->
                        Log.d(TAG, "fetched $loadedPresets")
                        _presets.value = loadedPresets
                        _presetsLoaded.update { true }
                    }
                    .addOnFailureListener {
                        Log.e(TAG, "Error fetching users: ${it.message}")
                    }
            }
            .addOnFailureListener {
                Log.e(TAG, "Error fetching presets: ${it.message}")
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
            "authorUid" to preset.authorUid,
            "bands" to preset.bands.map { mapOf("index" to it.first, "gain" to it.second.toInt()) } // conversione per firebase
        )

        return presetMap
    }

    /**
     * Add a preset to firestore database
     * @param preset Preset to be added
     */
    fun addPreset(preset: EqPreset) {
        Log.d(TAG, "Preset ${preset.name} to add to Firestore")

        presetsCollection.document(preset.id.toString())
            .set(presetMapFrom(preset), SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "Preset ${preset.name} saved to Firestore")
            }
            .addOnFailureListener {
                Log.e(TAG, "Errore saving: ${it.message}")
            }

        fetchPresets()
    }

    /**
     * Replace a preset
     * @param preset New preset, it will replace the current one with the same id
     */
    fun replacePreset(preset: EqPreset) {
        addPreset(preset) // 'set()' sovrascrive il documento su firebase
        fetchPresets()
    }
}