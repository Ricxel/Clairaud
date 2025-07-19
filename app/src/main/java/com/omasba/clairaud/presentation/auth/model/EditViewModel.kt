package com.omasba.clairaud.presentation.auth.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.omasba.clairaud.data.repository.AuthRepo
import com.omasba.clairaud.presentation.auth.state.AccountState
import com.omasba.clairaud.presentation.auth.state.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EditViewModel(
    val username: String,
    val email: String
): ViewModel(){
    private val _uiState = MutableStateFlow(AccountState(username, email))
    val uiState = _uiState.asStateFlow()

    fun onUsernameChanged(username: String){
        _uiState.update { it.copy(username = username) }
    }

    fun onEmailChanged(email: String){
        _uiState.update { it.copy(email = email) }
    }
    fun updateUserProfile(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = AuthRepo.updateUserData(UserProfile(username = _uiState.value.username, mail = _uiState.value.email))
            if(result.isFailure){
                _uiState.update {
                    it.copy(
                        error = result.exceptionOrNull()?.localizedMessage ?: "Undefined error",
                        isLoading = false
                    )
                }
            }
            else{
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