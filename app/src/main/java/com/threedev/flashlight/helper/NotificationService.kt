package com.threedev.flashlight.helper

import android.content.Context
import android.os.BatteryManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val selectedAppPackage = getSelectedAppPackage(this)
        val isNotificationEnabled = SessionManager.getBool(SessionManager.INCOMING_SMS, false)

        Log.d("NotificationService", "Notification received from: $packageName")

        if (!isNotificationEnabled) {
            Log.d("NotificationService", "Notification Flash Disabled")
            return
        }

        if (packageName == selectedAppPackage) {
            if (shouldFlash(applicationContext)) { // ✅ Check battery before flashing
                Log.d("NotificationService", "Flashing for app notification: $packageName")
                FlashLightManager.flashOnce(applicationContext, 200)
            } else {
                Log.d("NotificationService", "Battery too low, flashlight disabled.")
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {}

    private fun getSelectedAppPackage(context: Context): String? {
        return SessionManager.getString(SessionManager.SELECT_APP, "")
    }

    private fun shouldFlash(context: Context): Boolean {
        val batteryLevel = getBatteryPercentage(context)
        val minBatteryThreshold = SessionManager.getInt(SessionManager.BATTERY_SAVER_LEVEL, 20)
        return batteryLevel > minBatteryThreshold
    }

    private fun getBatteryPercentage(context: Context): Int {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }
}
