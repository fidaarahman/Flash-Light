package com.threedev.flashlight.ui.setting

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.threedev.flashlight.databinding.FragmentSettingBinding
import com.threedev.flashlight.helper.SessionManager
import com.threedev.flashlight.ui.SettingLanguageActivity
import java.util.*

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        setupTimePickers()
        setupDoNotDisturbToggle()
        setupBatterySaverToggle()
        loadSavedSettings()

        binding.llChangelanguage.setOnClickListener {
            val intent=Intent(requireContext(),SettingLanguageActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    private fun setupDoNotDisturbToggle() {
        binding.switchDoNotDisturb.setOnCheckedChangeListener { _, isChecked ->
            SessionManager.putBool(SessionManager.DND_SETTINGS_STATE, isChecked)

            if (isChecked) {
                enableTimeFields(true)
                saveDndSettings()
            } else {
                enableTimeFields(false)
                clearDndSettings()
            }
        }
    }

    private fun enableTimeFields(enable: Boolean) {
        binding.etFrom.isEnabled = enable
        binding.etTo.isEnabled = enable
        val textColor = if (enable) android.R.color.white else android.R.color.darker_gray
        binding.etFrom.setTextColor(resources.getColor(textColor, null))
        binding.etTo.setTextColor(resources.getColor(textColor, null))
    }

    private fun saveDndSettings() {
        val fromTime = binding.etFrom.text.toString()
        val toTime = binding.etTo.text.toString()

        if (fromTime.isNotEmpty() && toTime.isNotEmpty()) {
            SessionManager.setString(SessionManager.DND_FROM_TIME, fromTime)
            SessionManager.setString(SessionManager.DND_TO_TIME, toTime)
        }
    }

    private fun clearDndSettings() {
        SessionManager.setString(SessionManager.DND_FROM_TIME, "")
        SessionManager.setString(SessionManager.DND_TO_TIME, "")

        // Ensure flashlight remains active when DND is OFF
        SessionManager.putBool(SessionManager.MAIN_TOGGLE, true)

        binding.etFrom.text.clear()
        binding.etTo.text.clear()
    }

    private fun setupTimePickers() {
        binding.etFrom.setOnClickListener { showTimePicker(binding.etFrom) }
        binding.etTo.setOnClickListener { showTimePicker(binding.etTo) }
    }

    private fun showTimePicker(target: View) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(requireContext(), { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            (target as? android.widget.EditText)?.setText(formattedTime)
        }, hour, minute, true)

        timePicker.show()
    }

    // ✅ Setup Battery Saver Toggle
    private fun setupBatterySaverToggle() {
        binding.switchBatterySave.setOnCheckedChangeListener { _, isChecked ->
            SessionManager.putBool(SessionManager.BATTERY_SAVER_STATE, isChecked)

            if (isChecked) {
                val selectedBatteryLevel = binding.sliderFlashSpeed.value.toInt()
                SessionManager.setInt(SessionManager.BATTERY_SAVER_LEVEL, selectedBatteryLevel)
                checkBatteryLevelForFlash()
            } else {
                // When Battery Saver is OFF, enable Flashlight regardless of battery level
                SessionManager.putBool(SessionManager.FLASH_ALLOWED, true)
                Toast.makeText(requireContext(), "Battery Saver disabled. Flashlight will work normally.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.sliderFlashSpeed.addOnChangeListener { _, value, _ ->
            val selectedLevel = value.toInt()
            SessionManager.setInt(SessionManager.BATTERY_SAVER_LEVEL, selectedLevel)

            if (binding.switchBatterySave.isChecked) {
                checkBatteryLevelForFlash()
            }
        }
    }

    private fun checkBatteryLevelForFlash() {
        val batteryLevel = getBatteryPercentage()
        val selectedThreshold = SessionManager.getInt(SessionManager.BATTERY_SAVER_LEVEL, 20)

        if (binding.switchBatterySave.isChecked) {
            if (batteryLevel <= selectedThreshold) {
                // Prevent Flashlight if battery is low
                SessionManager.putBool(SessionManager.FLASH_ALLOWED, false)
                Toast.makeText(requireContext(), "Flashlight disabled due to low battery.", Toast.LENGTH_SHORT).show()
            } else {
                // Allow Flashlight if battery is fine
                SessionManager.putBool(SessionManager.FLASH_ALLOWED, true)
            }
        } else {
            // If Battery Saver is OFF, allow flashlight
            SessionManager.putBool(SessionManager.FLASH_ALLOWED, true)
        }
    }

    private fun getBatteryPercentage(): Int {
        val batteryManager = requireContext().getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    private fun loadSavedSettings() {
        val isDndEnabled = SessionManager.getBool(SessionManager.DND_SETTINGS_STATE, false)
        val fromTime = SessionManager.getString(SessionManager.DND_FROM_TIME, "")
        val toTime = SessionManager.getString(SessionManager.DND_TO_TIME, "")

        binding.switchDoNotDisturb.isChecked = isDndEnabled

        if (isDndEnabled) {
            if (fromTime.isNotEmpty()) binding.etFrom.setText(fromTime)
            if (toTime.isNotEmpty()) binding.etTo.setText(toTime)
            enableTimeFields(true)
        } else {
            enableTimeFields(false)
        }

        val isBatterySaverOn = SessionManager.getBool(SessionManager.BATTERY_SAVER_STATE, false)
        val savedBatteryLevel = SessionManager.getInt(SessionManager.BATTERY_SAVER_LEVEL, 20)

        binding.switchBatterySave.isChecked = isBatterySaverOn
        binding.sliderFlashSpeed.value = savedBatteryLevel.toFloat()

        // Ensure correct flash behavior is applied at launch
        checkBatteryLevelForFlash()
    }
}
