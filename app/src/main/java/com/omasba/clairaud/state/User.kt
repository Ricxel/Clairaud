package com.omasba.clairaud.state

data class User (
    var favPresets: Set<Int> = emptySet(), // lista degli id dei preset preferitit
    var uid: Int,
    var token: String,
    var username: String,
    var mail: String
)