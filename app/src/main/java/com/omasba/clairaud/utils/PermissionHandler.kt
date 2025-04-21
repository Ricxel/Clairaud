package com.omasba.clairaud.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHandler(private val activity: AppCompatActivity) {

    // Registro per richiedere permessi runtime
    private val requestPermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            onAllPermissionsGranted()
        } else {
            showPermissionExplanationDialog()
        }
    }

    // Array di permessi necessari per Android 13+
    private val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            android.Manifest.permission.POST_NOTIFICATIONS,
            android.Manifest.permission.FOREGROUND_SERVICE,
            android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        arrayOf(
            android.Manifest.permission.FOREGROUND_SERVICE
        )
    } else {
        arrayOf()
    }

    // Controlla e richiede tutti i permessi necessari
    fun checkAndRequestPermissions() {
        if (areAllPermissionsGranted()) {
            // Controlla il permesso NotificationListener, che richiede un trattamento speciale
            if (!isNotificationListenerEnabled()) {
                showNotificationListenerDialog()
            } else {
                onAllPermissionsGranted()
            }
        } else {
            requestPermissionLauncher.launch(requiredPermissions)
        }
    }

    // Verifica se tutti i permessi standard sono già concessi
    private fun areAllPermissionsGranted(): Boolean {
        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Verifica se il NotificationListener è abilitato
    fun isNotificationListenerEnabled(): Boolean {
        val packageName = activity.packageName
        val listeners = Settings.Secure.getString(
            activity.contentResolver,
            "enabled_notification_listeners"
        )
        return listeners?.contains(packageName) == true
    }

    // Mostra un dialogo per spiegare perché sono necessari i permessi
    private fun showPermissionExplanationDialog() {
        AlertDialog.Builder(activity)
            .setTitle("Permessi necessari")
            .setMessage("Per rilevare la musica in riproduzione e mostrate notifiche, l'app ha bisogno di alcuni permessi. Senza questi permessi, alcune funzionalità non saranno disponibili.")
            .setPositiveButton("Impostazioni") { _, _ -> openAppSettings() }
            .setNegativeButton("Annulla") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    // Mostra un dialogo specifico per il permesso NotificationListener
    private fun showNotificationListenerDialog() {
        AlertDialog.Builder(activity)
            .setTitle("Accesso alle notifiche")
            .setMessage("Per rilevare la musica in riproduzione da altre app, devi consentire l'accesso alle notifiche. Tocca 'Apri impostazioni' e attiva il servizio 'Clairaud Music Detection'.")
            .setPositiveButton("Apri impostazioni") { _, _ -> openNotificationListenerSettings() }
            .setNegativeButton("Più tardi") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    // Apre le impostazioni dell'app
    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", activity.packageName, null)
        }
        activity.startActivity(intent)
    }

    // Apre le impostazioni per il NotificationListener
    fun openNotificationListenerSettings() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        activity.startActivity(intent)
    }

    private fun onAllPermissionsGranted() {
        try {
            // Controlla se il NotificationListener è abilitato
            if (!isNotificationListenerEnabled()) {
                showNotificationListenerDialog()
                return
            }

            // Avvia il servizio di tracciamento
            val serviceIntent = Intent(activity, Class.forName("com.omasba.clairaud.components.MediaTrackingService"))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activity.startForegroundService(serviceIntent)
            } else {
                activity.startService(serviceIntent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Mostra un messaggio di errore all'utente
            AlertDialog.Builder(activity)
                .setTitle("Errore")
                .setMessage("Si è verificato un errore durante l'avvio del servizio: ${e.message}")
                .setPositiveButton("OK", null)
                .show()
        }
    }
}