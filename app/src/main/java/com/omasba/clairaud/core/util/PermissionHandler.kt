package com.omasba.clairaud.core.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHandler{
    companion object{

        private val NOTIFICATION_PERMISSION_CODE = 1

        /**
         *  Verify notification listener permission
         *  @param context Context
         *  @return Returns true if permission is granted, false otherwise
         */
        fun isNotificationServiceEnabled(context: Context): Boolean {
            val pkgName = context.packageName
            val enabledListeners = Settings.Secure.getString(
                context.contentResolver,
                "enabled_notification_listeners"
            )
            return enabledListeners?.contains(pkgName) == true
        }

        /**
         * Requests notification listener permission
         * @param context Context
         */
        fun requestNotificationAccess(context: Context) {
            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            context.startActivity(intent)
        }

        /**
         * Asks for post notification permission
         * @param context Context
         * @param activity Activity that will run the screen for granting the permission
         */
        fun askPostNotificationPermission(context: Context, activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // controlla a runtime la versione di android (>12)
                if (ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                        NOTIFICATION_PERMISSION_CODE
                    )
                }
            }
        }
    }
}