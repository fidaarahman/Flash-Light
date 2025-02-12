package com.threedev.flashlight.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.telephony.TelephonyManager
import android.util.Log

class CallReciever : BroadcastReceiver() {
    private val defaultBlinkSpeed: Long = 300

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.PHONE_STATE") {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)

            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                Log.d("call_checker", "Phone is ringing")

                if (shouldFlash(context)) { // ✅ Only flash if battery is above threshold
                    context?.let { FlashLightManager.startBlinking(it, defaultBlinkSpeed) }
                } else {
                    Log.d("call_checker", "Battery too low, flashlight disabled.")
                }
            } else {
                context?.let { FlashLightManager.stopBlinking(it) }
            }
        }
    }

    private fun shouldFlash(context: Context?): Boolean {
        val batteryLevel = getBatteryPercentage(context)
        val minBatteryThreshold = SessionManager.getInt(SessionManager.BATTERY_SAVER_LEVEL, 20)
        return batteryLevel > minBatteryThreshold
    }

    private fun getBatteryPercentage(context: Context?): Int {
        val batteryManager = context?.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }
}
