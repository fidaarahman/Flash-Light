package com.threedev.flashlight.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.telephony.TelephonyManager
import android.util.Log
<<<<<<< HEAD
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CallReciever : BroadcastReceiver() {
    private val defaultBlinkSpeed: Long = SessionManager.getInt(SessionManager.KEY_FLASH_BLINK_SPEED, 300).toLong()

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("call_checker", "Phone is ringing ${intent.action}")
        if (intent.action == "android.intent.action.PHONE_STATE") {
=======

class CallReciever : BroadcastReceiver() {
    private val defaultBlinkSpeed: Long = 300

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.PHONE_STATE") {
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)

            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                Log.d("call_checker", "Phone is ringing")

<<<<<<< HEAD
                if (shouldFlash(context) && !doNotDisturb()) { // ✅ Only flash if battery is above threshold
                    context.let { FlashLightManager.startBlinking(it, defaultBlinkSpeed) }
=======
                if (shouldFlash(context)) { // ✅ Only flash if battery is above threshold
                    context?.let { FlashLightManager.startBlinking(it, defaultBlinkSpeed) }
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
                } else {
                    Log.d("call_checker", "Battery too low, flashlight disabled.")
                }
            } else {
<<<<<<< HEAD
                context.let { FlashLightManager.stopBlinking() }
=======
                context?.let { FlashLightManager.stopBlinking(it) }
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
            }
        }
    }

    private fun shouldFlash(context: Context?): Boolean {
<<<<<<< HEAD
        val callingState = SessionManager.getBool(SessionManager.INCOMING_CALL_TOGGLE_STATE, false)
        val isMainToggleOn = SessionManager.getBool(SessionManager.MAIN_TOGGLE, false)
        val batteryLevel = getBatteryPercentage(context)
        val minBatteryThreshold = SessionManager.getInt(SessionManager.BATTERY_SAVER_LEVEL, 20)
        val status = batteryLevel > minBatteryThreshold

        return callingState && isMainToggleOn && status

=======
        val batteryLevel = getBatteryPercentage(context)
        val minBatteryThreshold = SessionManager.getInt(SessionManager.BATTERY_SAVER_LEVEL, 20)
        return batteryLevel > minBatteryThreshold
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
    }

    private fun getBatteryPercentage(context: Context?): Int {
        val batteryManager = context?.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }
<<<<<<< HEAD

    private fun doNotDisturb(): Boolean {
        val isDndEnabled = SessionManager.getBool(SessionManager.DND_SETTINGS_STATE, false)
        val fromTime = SessionManager.getString(SessionManager.DND_FROM_TIME, "")
        val toTime = SessionManager.getString(SessionManager.DND_TO_TIME, "")
        if (!isDndEnabled) {
            return false
        }

        if (fromTime.isNotEmpty() && toTime.isNotEmpty()) {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val now = Calendar.getInstance()
            val fromCal = Calendar.getInstance()
            val toCal = Calendar.getInstance()
            fromCal.time = sdf.parse(fromTime)!!
            toCal.time = sdf.parse(toTime)!!

            // Set today's date for from and to
            fromCal.set(Calendar.YEAR, now.get(Calendar.YEAR))
            fromCal.set(Calendar.MONTH, now.get(Calendar.MONTH))
            fromCal.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH))

            toCal.set(Calendar.YEAR, now.get(Calendar.YEAR))
            toCal.set(Calendar.MONTH, now.get(Calendar.MONTH))
            toCal.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH))

            // If toTime is before fromTime, it means the range crosses midnight
            if (toCal.before(fromCal)) {
                // Move toTime to next day
                toCal.add(Calendar.DATE, 1)

            }
            val status = now.timeInMillis in fromCal.timeInMillis..toCal.timeInMillis
            Log.d("NotificationService", "status = > $status")
            return status
        }
        return false
    }
=======
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
}
