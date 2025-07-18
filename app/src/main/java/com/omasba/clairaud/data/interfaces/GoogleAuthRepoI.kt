package com.omasba.clairaud.data.interfaces

interface GoogleAuthRepoI {
    suspend fun loginWithGoogle(idToken: String): Result<Unit>
}