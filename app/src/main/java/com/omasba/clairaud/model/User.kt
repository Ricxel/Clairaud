package com.omasba.clairaud.model

import com.omasba.clairaud.components.StoreRepo

class User (
    var favPresets: Set<Int> = emptySet(), // lista degli id dei preset preferitit
    var uid: String,
    var token: String,
    var username: String,
    var mail: String
){
    // per modificare i dati dal profilo
    fun copy(
        favPresets: Set<Int> = this.favPresets,
        username: String = this.username,
        mail: String = this.mail
    ): User {
        return User(favPresets, uid, token, username, mail)
    }
}