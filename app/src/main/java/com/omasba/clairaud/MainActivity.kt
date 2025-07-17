package com.omasba.clairaud

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.omasba.clairaud.ui.MainScreen
import com.omasba.clairaud.ui.theme.ClairaudTheme
import com.omasba.clairaud.utils.PermissionHandler

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