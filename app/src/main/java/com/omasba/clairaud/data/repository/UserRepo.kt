package com.omasba.clairaud.data.repository

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.omasba.clairaud.presentation.auth.state.UserProfile
import com.omasba.clairaud.presentation.store.state.EqPreset
import com.omasba.clairaud.presentation.store.state.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * User state holder repository
 */
object UserRepo {
    private const val TAG = "UserRepo"
    var currentUserProfile: UserProfile = UserProfile()
    private var _favPresets = MutableStateFlow<Set<Int>>(currentUserProfile.favPresets)
    val favPresets = _favPresets.asStateFlow()

    fun getPresetToApply(tags: Set<Tag>): EqPreset {
        //prendo i preset dallo store
        var maxCount = 0
        var correctPreset = EqPreset()

        _favPresets.value.forEach{ id ->
            run {
                val preset = StoreRepo.presets.value.find { it.id == id } ?: EqPreset()
                val tagsIntersection = preset.tags intersect tags
                if (tagsIntersection.count() > maxCount){
                    maxCount = tagsIntersection.count()
                    correctPreset = preset
                }
            }
        }
        return correctPreset
    }

    fun setFavPresets(){
        val uid = currentUserProfile.uid

        val favList = _favPresets.value.toList()  // List<Int> will be saved as array

        Firebase.firestore.collection("users").document(uid)
            .update("favPresets", favList)
            .addOnSuccessListener {
                Log.d(TAG, "Favorites successfully updated in Firestore.")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error updating favorites in Firestore", e)
            }

    }

    fun getFavPresets() {
        val uid = currentUserProfile.uid
        try {
            Firebase.firestore.collection("users").document(uid).get()
                .addOnSuccessListener { result ->
                    try {
                        val favs = result.get("favPresets") as? List<*>
                        val ids = favs?.mapNotNull { (it as? Number)?.toInt() }?.toSet() ?: emptySet()

                        _favPresets.value = ids // ✅ aggiorni lo StateFlow

                        Log.d(TAG, "Preferiti caricati: $ids")
                    } catch (e: Exception) {
                        Log.e(TAG, "Errore parsing favPresets: ${e.message}")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Errore Firestore get(): ${e.message}")
                }
        }catch (e:Exception){
            Log.e(TAG, e.message.toString())
        }

    }


    /**
     * @param id Preset ID
     * @return true if the specified preset is a favourite one by the current user
     */
    fun isFavorite(id: Int): Boolean {
        return _favPresets.value.contains(id);
    }

    /**
     * Add to user's favourites a preset
     * @param id Preset Id
     */
    fun addFavorite(id: Int) {
        _favPresets.update { it + id }
        setFavPresets()
    }

    /**
     * Remove a preset from the user's favourites
     * @param id Preset ID to remove
     */
    fun removeFavorite(id: Int) {
        _favPresets.update { it - id }
        setFavPresets()
    }

    init {
        //getFavPresets()
    }
}