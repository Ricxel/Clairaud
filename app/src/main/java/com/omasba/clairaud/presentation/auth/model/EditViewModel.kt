package com.omasba.clairaud.presentation.auth.model

import androidx.lifecycle.ViewModel
import com.omasba.clairaud.presentation.auth.state.AccountState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(AccountState())
    val uiState = _uiState.asStateFlow()

    fun onUsernameChanged(username: String){
        _uiState.update { it.copy(username = username) }
    }

    fun onEmailChanged(email: String){
        _uiState.update { it.copy(email = email) }
    }


}