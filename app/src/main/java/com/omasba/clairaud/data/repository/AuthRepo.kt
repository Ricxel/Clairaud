package com.omasba.clairaud.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.omasba.clairaud.data.interfaces.AuthRepoI
import com.omasba.clairaud.data.interfaces.GoogleAuthRepoI
import kotlinx.coroutines.tasks.await

object AuthRepo: AuthRepoI, GoogleAuthRepoI {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun register(email: String, password: String): Result<Unit> = runCatching {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    override fun getCurrentUser(): FirebaseUser?{
        return firebaseAuth.currentUser
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun loginWithGoogle(idToken: String): Result<Unit> = runCatching {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).await()
    }
}