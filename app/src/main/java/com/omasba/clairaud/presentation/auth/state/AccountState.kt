package com.omasba.clairaud.presentation.auth.state

data class AccountState(
    val username: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isChanged: Boolean = false
)