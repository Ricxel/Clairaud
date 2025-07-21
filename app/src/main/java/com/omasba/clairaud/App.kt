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
    override fun onCreate() {
        super.onCreate()


        //avvio il servizio per la rilevazione delle sessioni audio
        //Il servizio è necessario solo nelle versioni recenti di android (>=26) altrimenti è sufficiente creare l'eq con session_id = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startEqService()
        } else {
            //uso session_id=0
            val eq = Eq(0)

            EqRepo.setEq(eq)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startEqService() {
        val serviceIntent = Intent(this, EqService::class.java)

        startForegroundService(serviceIntent)
        Log.d("EqScreen", "Started!")
    }

    override fun onTerminate() {
        super.onTerminate()
        UserRepo.logout()
    }
}