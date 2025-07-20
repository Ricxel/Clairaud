package com.omasba.clairaud.data.repository

import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.test.espresso.core.internal.deps.dagger.Reusable
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.omasba.clairaud.data.interfaces.AuthRepoI
import com.omasba.clairaud.data.interfaces.GoogleAuthRepoI
import com.omasba.clairaud.presentation.auth.state.UserProfile
import com.omasba.clairaud.presentation.auth.state.UserProfileDTO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

/**
 * User state auth repository
 */
object AuthRepo: AuthRepoI {
    private val TAG = "auth"
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance() //per l'auth

    val USERNAME_MAX_DIM = 30

    /**
     * Authenticates the user
     * @param email user's email
     * @param password user's password
     */
    override suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    /**
     * Registers a new user
     * @param email user's email
     * @param password user's password
     */
    override suspend fun register(email: String, password: String): Result<Unit> = runCatching {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    /**
     * @return the current authenticated user
     */
    override fun getCurrentUser(): FirebaseUser?{
        return firebaseAuth.currentUser
    }

    /**
     * Creates a new user profile
     * @param uid the user's uid
     * @param profile the user's email and password
     */
    override suspend fun createUserProfile(uid: String, profile: UserProfile): Result<Unit> = runCatching{
        Log.d("auth","Inizio profilo")

        //converto perchè firebase non support i set
        val tmp: UserProfileDTO = UserProfileDTO(
            favPresets = profile.favPresets.toList(),
            uid = profile.uid,
            username = profile.username,
            mail = profile.mail
        )
        FirebaseFirestore.getInstance().collection("users").document(uid).set(tmp).await()
        Log.d("auth","Creato $tmp")
    }

    /**
     * @param uid the user's you are looking for
     * @return the given user's profile
     */
    override suspend fun getUserProfile(uid: String): Result<UserProfile> = runCatching {
        val snapshot = FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .get()
            .await()

        if (!snapshot.exists()) {
            throw IllegalStateException("User profile not found for uid=$uid")
        }
        Log.d("auth", "Snapshot ${snapshot.data}")

        val profileDTO = snapshot.toObject(UserProfileDTO::class.java)
            ?: throw IllegalStateException("Cannot parse UserProfileDTO from Firestore")

        Log.d("auth", "ProfiloDTO $profileDTO")

        UserProfile(
            favPresets = profileDTO.favPresets.toSet(),
            mail = profileDTO.mail,
            username = profileDTO.username,
            uid = profileDTO.uid
        )
    }

    /**
     * logs out the current user
     */
    override fun logout() {
        firebaseAuth.signOut()
    }

    /**
     * Update's the user profile
     * @param profile the user's profile
     */
    override suspend fun updateUserData(profile: UserProfile): Result<Unit> = runCatching {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser ?: throw IllegalStateException("User not logged in")

        if (profile.mail != user.email) {
            FirebaseAuth.getInstance().currentUser?.updateEmail(profile.mail)
        }

        val dto = UserProfileDTO(
            favPresets = UserRepo.currentUserProfile.favPresets.toList(),
            uid = user.uid,
            username = profile.username,
            mail = profile.mail
        )

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(user.uid)
            .set(dto) // sovrascrive tutto il documento
            .await()

        //aggiorna anche il currentUserProfile nel repo
        UserRepo.currentUserProfile = UserRepo.currentUserProfile.copy(username = profile.username, mail = profile.mail)
        Log.d("auth", "Cambiato profilo")
    }
}