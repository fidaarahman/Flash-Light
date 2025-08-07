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
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.threedev.flashlight.R
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

        loadSavedSettings()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvFrom.setOnClickListener { showTimePicker(binding.tvFrom) }
        binding.tvTo.setOnClickListener { showTimePicker(binding.tvTo) }

        binding.llChangelanguage.setOnClickListener {
            val intent=Intent(requireContext(),SettingLanguageActivity::class.java)
            startActivity(intent)
        }

        binding.switchDoNotDisturb.setOnCheckedChangeListener { _, isChecked ->
            SessionManager.putBool(SessionManager.DND_SETTINGS_STATE, isChecked)
            enableTimeFields(isChecked)
        }

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

    private fun enableTimeFields(enable: Boolean) {
        binding.tvFrom.isEnabled = enable
        binding.tvTo.isEnabled = enable
        val textColor = if (enable) R.color.active_color else R.color.in_active_color
        binding.tvFrom.setTextColor(resources.getColor(textColor, null))
        binding.tvTo.setTextColor(resources.getColor(textColor, null))
    }

    private fun showTimePicker(target: TextView) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(requireContext(), { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            target.text = formattedTime
            if (target.id == R.id.tv_from) {
                SessionManager.setString(SessionManager.DND_FROM_TIME, formattedTime)
            }else{
                SessionManager.setString(SessionManager.DND_TO_TIME, formattedTime)
            }

        }, hour, minute, true)

        timePicker.show()
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
        if (fromTime.isNotEmpty()) binding.tvFrom.text = fromTime
        if (toTime.isNotEmpty()) binding.tvTo.text = toTime
        enableTimeFields(isDndEnabled)

        val isBatterySaverOn = SessionManager.getBool(SessionManager.BATTERY_SAVER_STATE, false)
        val savedBatteryLevel = SessionManager.getInt(SessionManager.BATTERY_SAVER_LEVEL, 20)

        binding.switchBatterySave.isChecked = isBatterySaverOn
        binding.sliderFlashSpeed.value = savedBatteryLevel.toFloat()

        checkBatteryLevelForFlash()
    }
}
