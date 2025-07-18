package com.omasba.clairaud.state

data class AuthUiState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val email: String = "",
    val password: String = "",
    val username: String = ""
)