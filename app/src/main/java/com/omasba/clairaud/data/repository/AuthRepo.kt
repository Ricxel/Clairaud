package com.omasba.clairaud.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.omasba.clairaud.data.interfaces.AuthRepoI
import com.omasba.clairaud.data.interfaces.GoogleAuthRepoI
import com.omasba.clairaud.presentation.auth.state.UserProfile
import com.omasba.clairaud.presentation.auth.state.UserProfileDTO
import kotlinx.coroutines.tasks.await

object AuthRepo: AuthRepoI, GoogleAuthRepoI {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance() //per l'auth

    override suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun register(email: String, password: String): Result<Unit> = runCatching {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    override fun getCurrentUser(): FirebaseUser?{
        return firebaseAuth.currentUser
    }

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


    override fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun loginWithGoogle(idToken: String): Result<Unit> = runCatching {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).await()
    }
}