package com.omasba.clairaud.ui.models

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasba.clairaud.data.repository.AuthRepo
import com.omasba.clairaud.state.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel(){
    private var _uiState = MutableStateFlow<AuthUiState>(AuthUiState())
    var uiState = _uiState.asStateFlow()

    fun onEmailChanged(email: String){
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password:String){
        _uiState.update { it.copy(password = password) }
    }

    fun login(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) } //metto a loading

            val result = AuthRepo.login(_uiState.value.email, _uiState.value.password)
            _uiState.update {
                if (result.isSuccess){
                    it.copy(isLoading = false, isLoggedIn = true)
                }
                else{
                    //ritorno l'oggetto con un errore
                    it.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.localizedMessage ?: "Undefined error"
                    )
                }
            }

        }
    }
    fun register(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) } //metto a loading

            val result = AuthRepo.register(_uiState.value.email, _uiState.value.password)
            _uiState.update {
                if (result.isSuccess){
                    it.copy(isLoading = false, isLoggedIn = true)
                }
                else{
                    //ritorno l'oggetto con un errore
                    it.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.localizedMessage ?: "Undefined error"
                    )
                }
            }

        }
    }
    fun logout(){
        AuthRepo.logout() //logiut
        _uiState.value = AuthUiState() // resetto anche la ui
    }

}