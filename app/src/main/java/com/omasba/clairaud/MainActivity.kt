package com.omasba.clairaud

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.omasba.clairaud.ui.theme.ClairaudTheme
import com.omasba.clairaud.autoeq.utils.AutoEqualizerUtils
import com.omasba.clairaud.autoeq.ui.AutoEq
import com.omasba.clairaud.autoeq.ui.AutoEqViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!AutoEqualizerUtils.isNotificationServiceEnabled(this)) // richiede i permessi se non ci sono
            AutoEqualizerUtils.requestNotificationAccess(this)
        val autoEqView = AutoEqViewModel()
        setContent {
            ClairaudTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Clairaud/AutoEQ", fontSize = 40.sp)
                        AutoEq(autoEqView)
                    }
                }
            }
        }
    }
    private fun isNotificationListenerEnabled(): Boolean {
        val packageName = packageName
        val listeners = Settings.Secure.getString(
            contentResolver,
            "enabled_notification_listeners"
        )
        return listeners?.contains(packageName) == true
    }

    private fun showEnableNotificationListenerDialog() {
        AlertDialog.Builder(this)
            .setTitle("Attiva il rilevamento musica")
            .setMessage("Per rilevare la musica in riproduzione, devi abilitare l'accesso alle notifiche. Premi OK per andare alle impostazioni.")
            .setPositiveButton("OK") { _, _ -> openNotificationListenerSettings() }
            .setNegativeButton("Più tardi", null)
            .show()
    }

    private fun openNotificationListenerSettings() {
        startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
    }
}