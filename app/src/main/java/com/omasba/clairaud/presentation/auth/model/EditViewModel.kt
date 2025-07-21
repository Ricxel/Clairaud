package com.omasba.clairaud.presentation.auth.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasba.clairaud.data.repository.AuthRepo
import com.omasba.clairaud.presentation.auth.state.AccountState
import com.omasba.clairaud.presentation.auth.state.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditViewModel(
    val username: String,
    val email: String
) : ViewModel() {
    private val USERNAME_MAX_DIM = 30
    private val _uiState = MutableStateFlow(AccountState(username, email))
    val uiState = _uiState.asStateFlow()

    fun onUsernameChanged(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email.trim()) }
    }

    fun updateUserProfile() {
        //sanifico
        _uiState.update {
            it.copy(
                username = it.username.trim(),
                email = it.email.trim()
            )
        }
        val value = _uiState.value
        if (value.email.isBlank()) {
            _uiState.update { it.copy(error = "Email field cannot be blank") }
            return
        }
        if (value.username.isBlank() || value.username.length >= USERNAME_MAX_DIM) {
            _uiState.update { it.copy(error = "Username canot be blank or have more than $USERNAME_MAX_DIM characters") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = AuthRepo.updateUserData(
                UserProfile(
                    username = _uiState.value.username,
                    mail = _uiState.value.email
                )
            )
            if (result.isFailure) {
                _uiState.update {
                    it.copy(
                        error = result.exceptionOrNull()?.localizedMessage ?: "Undefined error",
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isChanged = true
                    )
                }
            }
        }
    }


}