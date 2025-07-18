package com.omasba.clairaud.data.interfaces

import com.google.firebase.auth.FirebaseUser
import com.omasba.clairaud.presentation.auth.state.UserProfile

interface AuthRepoI {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(email: String, password: String): Result<Unit>
    fun getCurrentUser(): FirebaseUser?
    suspend fun createUserProfile(uid: String, profile: UserProfile): Result<Unit>
    suspend fun getUserProfile(uid: String): Result<UserProfile>
    fun logout()
}