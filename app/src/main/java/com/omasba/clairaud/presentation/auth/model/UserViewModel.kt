package com.omasba.clairaud.presentation.auth.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasba.clairaud.presentation.auth.state.UserProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _userProfileData = MutableStateFlow<UserProfile?>(null)
    val userProfileData: StateFlow<UserProfile?> = _userProfileData.asStateFlow()

    private var initialized = false

    init {
        loadUser()
    }

    fun loadUser() {
        if (initialized) return

        viewModelScope.launch {
            _userProfileData.value = UserProfile(
                favPresets = emptySet(),
                uid = "123",
                username = "ClairaudUser",
                mail = "user@clairaud.com"
            )
            initialized = true
        }
    }
}
