package com.omasba.clairaud.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.omasba.clairaud.presentation.auth.state.UserProfile
import com.omasba.clairaud.presentation.home.state.AutoEqStateHolder
import com.omasba.clairaud.presentation.store.state.EqPreset
import com.omasba.clairaud.presentation.store.state.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await

/**
 * User state holder repository
 */
object UserRepo {

    private const val TAG = "UserRepo"

    var currentUserProfile: UserProfile = UserProfile()
    private var _favPresets = MutableStateFlow<Set<Int>>(currentUserProfile.favPresets)
    val favPresets = _favPresets.asStateFlow()


    fun getPresetToApply(tags: Set<Tag>): EqPreset {
        // prendo i preset dallo store
        var maxCount = 0
        var correctPreset = EqPreset()

        _favPresets.value.forEach { id ->
            run {
                val preset = StoreRepo.presets.value.find { it.id == id } ?: EqPreset()
                val tagsIntersection = preset.tags intersect tags
                if (tagsIntersection.count() > maxCount) {
                    maxCount = tagsIntersection.count()
                    correctPreset = preset
                }
            }
        }
        return correctPreset
    }

    /**
     * Uploads to Firestore a new list of favourite presets (for the current user)
     */
    fun setFavPresets() {
        val uid = currentUserProfile.uid

        val favList = _favPresets.value.toList()  // List<Int> sara' salvata come array su Firestore

        Firebase.firestore.collection("users").document(uid)
            .update("favPresets", favList)
            .addOnSuccessListener {
                Log.d(TAG, "Favorites successfully updated in Firestore.")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error updating favorites in Firestore", e)
            }

    }

    /**
     * Retrieves the current user's favourite presets from Firestore
     */
    fun getFavPresets() {
        val uid = currentUserProfile.uid
        try {
            Firebase.firestore.collection("users").document(uid).get()
                .addOnSuccessListener { result ->
                    try {
                        val favs = result.get("favPresets") as? List<*>
                        val ids =
                            favs?.mapNotNull { (it as? Number)?.toInt() }?.toSet() ?: emptySet()

                        _favPresets.value = ids // aggiorna lo StateFlow

                        Log.d(TAG, "Favourites loaded: $ids")
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing favPresets: ${e.message}")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error Firestore get(): ${e.message}")
                }
        } catch (e: Exception) {
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

    /**
     * Verify if the user is logged, and token's validity
     */
    suspend fun isLogged(): Boolean {
        //controllo firebase
        val firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser ?: return false


        return try {
            // forza un refresh del token per verificarne la validità
            val tokenResult = firebaseUser.getIdToken(true).await()
            tokenResult.token != null
        } catch (e: Exception) {
            //  l'utente non è autenticato
            false
        }
    }

    /**
     * Logs out the current user
     */
    fun logout() {
        AuthRepo.logout()
        currentUserProfile = UserProfile()
        StoreRepo.reset()
        AutoEqStateHolder.setIsOn(false)
    }

    /**
     * Checks user authentication and set the local user
     */
    suspend fun authOnStart() {
        //controllo la validità del token
        if (isLogged()) {
            //imposto l'utente locale
            val uid = AuthRepo.getCurrentUser()?.uid

            if (uid != null)
                currentUserProfile = AuthRepo.getUserProfile(uid).getOrNull() ?: UserProfile()
        }
    }
}