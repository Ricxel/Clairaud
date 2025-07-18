package com.omasba.clairaud

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.omasba.clairaud.presentation.home.MainScreen
import com.omasba.clairaud.presentation.theme.ClairaudTheme
import com.omasba.clairaud.core.util.PermissionHandler

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PermissionHandler.askPostNotificationPermission(this,this)

        if(!PermissionHandler.isNotificationServiceEnabled(this)) // richiede i permessi se non ci sono
            PermissionHandler.requestNotificationAccess(this)

        setContent {
            ClairaudTheme {
                MainScreen()
            }
        }
    }
}