package com.omasba.clairaud.presentation.auth.state

data class UserProfileDTO(
    var favPresets: List<Int> = emptyList(), // lista degli id dei preset preferitit
    var uid: String = "",
    var username: String = "",
    var mail: String = ""
)