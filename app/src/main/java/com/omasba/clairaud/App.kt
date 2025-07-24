package com.omasba.clairaud

import android.app.Application
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.omasba.clairaud.data.repository.EqRepo
import com.omasba.clairaud.data.repository.UserRepo
import com.omasba.clairaud.service.eq.Eq
import com.omasba.clairaud.service.eq.EqService

class App : Application() {

    override fun onTerminate() {
        super.onTerminate()
        UserRepo.logout()
    }
}