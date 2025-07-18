package com.omasba.clairaud.data.interfaces

import com.google.firebase.auth.FirebaseUser

interface AuthRepoI {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(email: String, password: String): Result<Unit>
    fun getCurrentUser(): FirebaseUser?
    fun logout()
}