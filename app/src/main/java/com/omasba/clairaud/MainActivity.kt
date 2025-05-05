package com.omasba.clairaud

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.omasba.clairaud.autoeq.utils.AutoEqualizerUtils
import com.omasba.clairaud.ui.MainScreen
import com.omasba.clairaud.ui.theme.ClairaudTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!AutoEqualizerUtils.isNotificationServiceEnabled(this)) // richiede i permessi se non ci sono
            AutoEqualizerUtils.requestNotificationAccess(this)

        setContent {
            ClairaudTheme {
                MainScreen()
            }
        }
    }
}