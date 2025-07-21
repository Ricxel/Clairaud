package com.omasba.clairaud.presentation.auth.model

import android.util.Log
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User
import com.omasba.clairaud.data.repository.AuthRepo
import com.omasba.clairaud.data.repository.UserRepo
import com.omasba.clairaud.presentation.auth.state.AuthUiState
import com.omasba.clairaud.presentation.auth.state.UserProfile
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

    fun onUsernameChange(username: String){
        _uiState.update { it.copy(username = username) }
    }

    /**
     * Checks provided params
     * @param checkUsername Specify if the function must check the username or not
     * @return True if all params are valid, false otherwise
     */
    private fun checkParams(checkUsername: Boolean = false): Boolean{
        _uiState.update {
            it.copy(
                username = it.username.trim(),
                email = it.email.trim(),
            )
        }
        if(_uiState.value.email.isBlank()){
            _uiState.update { it.copy(error = "Email is blank") }
            return false
        }
        if((_uiState.value.username.isBlank() || _uiState.value.username.length >= AuthRepo.USERNAME_MAX_DIM)&& checkUsername){
            _uiState.update { it.copy(error = "Username is not valid") }
            return false
        }
        if(_uiState.value.password.isBlank()){
            _uiState.update { it.copy(error = "Password is blank") }
            return false
        }

        return true
    }
    /**
     * Logs in a user with the provided email and password
     * Then fetches the user profile and favorite presets
     */
    fun login(){
        //filtro e sanifica
        if(!checkParams()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) } //metto a loading

            val result = AuthRepo.login(_uiState.value.email, _uiState.value.password)
            _uiState.update {
                if (result.isSuccess){
                    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    val profile = AuthRepo.getUserProfile(uid).getOrNull() ?: UserProfile()

                    UserRepo.currentUserProfile = profile
                    Log.d("auth", "Current set $profile")
                    it.copy(isLoading = false, isLoggedIn = true, email = "", password = "")
                }
                else{
                    //ritorno l'oggetto con un errore
                    it.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.localizedMessage ?: "Undefined error"
                    )
                }
            }

            UserRepo.getFavPresets()
        }
    }
    
    /**
     * Registers a new user with the provided email, password, and username
     */
    fun register(){
        //filtro e sanifica
        if(!checkParams(true)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) } //metto a loading

            val result = AuthRepo.register(_uiState.value.email, _uiState.value.password)
            _uiState.update {
                if (result.isSuccess){
                    val user = FirebaseAuth.getInstance().currentUser //prendo l'utente per avere l'id e creare il profilo

                    if(user != null){
                        val profile = UserProfile(
                            uid = user.uid,
                            username = _uiState.value.username,
                            mail = _uiState.value.email,
                        )

                        AuthRepo.createUserProfile(user.uid, profile) //creo il profilo
                        UserRepo.currentUserProfile = profile
                        it.copy(isLoading = false, isLoggedIn = true, email = "", password = "", username = "")
                    }
                    else it
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

    /**
     * Logs out the current user
     */
    fun logout(){
        AuthRepo.logout()
        _uiState.value = AuthUiState() // resetto anche la ui
        UserRepo.logout()
    }

}