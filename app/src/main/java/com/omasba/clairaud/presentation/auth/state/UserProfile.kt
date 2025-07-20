package com.omasba.clairaud.presentation.auth.state

data class UserProfile (
    var favPresets: Set<Int> = emptySet(), // lista degli id dei preset preferiti
    var uid: String = "",
    var username: String = "",
    var mail: String = ""
)