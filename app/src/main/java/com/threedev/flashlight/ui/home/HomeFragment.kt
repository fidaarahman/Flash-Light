package com.threedev.flashlight.ui.home

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.app.ActivityCompat.getColor
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.threedev.flashlight.R
import com.threedev.flashlight.databinding.FragmentHomeBinding
import com.threedev.flashlight.helper.CallReciever
import com.threedev.flashlight.helper.FlashLightManager
import com.threedev.flashlight.helper.NotificationService
import com.threedev.flashlight.helper.SessionManager
import com.threedev.flashlight.helper.SmsReciver
import com.threedev.flashlight.ui.FlashTypeBottomSheet
import com.threedev.flashlight.ui.MessageDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var isBlinking=false
    private var blinkspeed : Long=300
    private var isRhythmMode = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toggleOn=SessionManager.getBool("MAIN_TOGGLE_STATE",false)
        val isNotificationSmsEnabled = SessionManager.getBool(SessionManager.INCOMING_SMS, false)
        binding.switchNotificationSms.isOn = isNotificationSmsEnabled

        binding.mainToggle.isOn=toggleOn
        initializeCardStates(toggleOn)

        binding.llFlashType.setOnClickListener {  }
        binding.llSelectApp.setOnClickListener {
            if (isNotificationAccessGranted(requireContext())) {

                findNavController().navigate(R.id.action_Home_to_Moreapps)
                Log.d("Notification_permission","Permission for Notification is granted")
            }
            else{
                val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                startActivity(intent)
                Log.d("Notification_permission","Permission is not granted")
            }
        }
        binding.sliderFlashSpeed.addOnChangeListener { _, value, _ ->
            blinkspeed = value.toLong().coerceAtLeast(100)
            binding.tvSpeed.text = "$blinkspeed ms"

            Log.d("SliderFlashSpeed", "Blink speed updated: $blinkspeed ms")

            if (isBlinking) {
                FlashLightManager.stopBlinking(requireContext())
                CoroutineScope(Dispatchers.Main).launch {
                    delay(100)
                    FlashLightManager.startBlinking(requireContext(), blinkspeed)
                }
            }
        }

        binding.llFlashType.setOnClickListener {
            showFlashTypeBottomSheet()
        }
        binding.btnTest.setOnClickListener {
            FlashLightManager.startBlinking(requireContext(), blinkspeed)

            CoroutineScope(Dispatchers.Main).launch {
                delay(3000)
                FlashLightManager.stopBlinking(requireContext())
            }
        }

        binding.mainToggle.setOnToggledListener { _, isChecked ->
            val hasAllPermissions = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED

            if (hasAllPermissions) {
                SessionManager.putBool(SessionManager.MAIN_TOGGLE, isChecked)
                Log.d("MainToggleState", "Main toggle changed. New state: $isChecked")
                toggleCardState(binding.cvIncomingCall, isChecked)
                toggleCardState(binding.cvNotificationSms, isChecked)
                toggleCardState(binding.cvFlashSettings, isChecked)
            } else {
                binding.mainToggle.isOn = false
                Dexter.withContext(requireContext())
                    .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_PHONE_STATE
                    )
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                            if (report.areAllPermissionsGranted()) {
                                SessionManager.putBool(SessionManager.MAIN_TOGGLE, isChecked)
                                Log.d("MainToggleState", "Main toggle changed. New state: $isChecked")
                                toggleCardState(binding.cvIncomingCall, isChecked)
                                toggleCardState(binding.cvNotificationSms, isChecked)
                                toggleCardState(binding.cvFlashSettings, isChecked)
                            } else if (report.isAnyPermissionPermanentlyDenied) {
                                binding.mainToggle.isOn = false
                                MessageDialogBuilder.Builder(requireActivity())
                                    .withTitle("Permission Denied Permanently")
                                    .withMessage("Go to settings and give permission.")
                                    .withOkButtonListener("Go to Settings", object : MessageDialogBuilder.OnOkClick {
                                        override fun onClick(dialogs: androidx.appcompat.app.AlertDialog) {
                                            dialogs.dismiss()
                                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                            val uri = Uri.fromParts("package", requireActivity().packageName, null)
                                            intent.data = uri
                                            startActivity(intent)
                                        }
                                    }).build()
                            } else {
                                binding.mainToggle.isOn = false
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permissions: MutableList<PermissionRequest>,
                            token: PermissionToken
                        ) {
                            binding.mainToggle.isOn = false
                            token.continuePermissionRequest()
                        }
                    }).check()
            }
        }

        binding.switchIncomingCall.setOnToggledListener { _, isChecked ->
            SessionManager.putBool(SessionManager.INCOMING_CALL_TOGGLE_STATE,isChecked)
            if (isChecked) {
                val telephonyManager = requireContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val phoneStateListener = object : PhoneStateListener() {
                    override fun onCallStateChanged(state: Int, incomingNumber: String?) {
                        super.onCallStateChanged(state, incomingNumber)

                        when (state) {
                            TelephonyManager.CALL_STATE_RINGING -> {
                                if (!isBlinking) {
                                    Log.d("CallChecker", "Incoming call from: $incomingNumber")
                                    FlashLightManager.startBlinking(requireContext(), blinkspeed)
                                    isBlinking = true
                                }
                            }
                            TelephonyManager.CALL_STATE_IDLE -> {
                                if (isBlinking) {
                                    FlashLightManager.stopBlinking(requireContext())
                                    isBlinking = false
                                }
                            }
                        }
                    }
                }

                telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
            }
            else {
                if (isBlinking) {
                    FlashLightManager.stopBlinking(requireContext())
                    isBlinking = false
                }
            }

        }

        binding.switchNotificationSms.setOnToggledListener { _, isChecked ->

            SessionManager.putBool(SessionManager.INCOMING_SMS, isChecked)
            SessionManager.putBool(SessionManager.INCOMING_NOTIFICATIONS, isChecked)

            if (isChecked) {
                if (!isNotificationAccessGranted(requireContext())) {
                    Toast.makeText(requireContext(), "Please enable notification access for this feature.", Toast.LENGTH_LONG).show()
                    val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                    startActivity(intent)
                } else {
                    Log.d("NotificationService", "Notification-based Flashlight Enabled")
                }
            } else {
                FlashLightManager.stopBlinking(requireContext())
                Log.d("NotificationService", "Notification-based Flashlight Disabled")
            }
        }
    }

    private fun showFlashTypeBottomSheet() {
        val bottomSheet = FlashTypeBottomSheet(requireContext(), R.style.TransparentBottomSheetDialog) { flashType ->
            when (flashType) {
                "Continuous" -> {
                    isRhythmMode = false
                    SessionManager.putBool("FLASH_MODE_RHYTHM", false)
                }
                "Rhythm" -> {
                    isRhythmMode = true
                    SessionManager.putBool("FLASH_MODE_RHYTHM", true)
                }
            }
        }
        bottomSheet.show(parentFragmentManager, "FlashTypeBottomSheet")
    }

    private fun toggleCardState(cardView: ViewGroup, isEnabled: Boolean) {
        cardView.isEnabled = isEnabled
        cardView.alpha = if (isEnabled) 1f else 0.5f
        for (i in 0 until cardView.childCount) {
            val child = cardView.getChildAt(i)
            child.isEnabled = isEnabled

            if (child is ViewGroup) {
                for (j in 0 until child.childCount) {
                    val innerChild = child.getChildAt(j)
                    innerChild.isEnabled = isEnabled
                }
            }

        }
    }

    private fun initializeCardStates(isOn: Boolean) {
        toggleCardState(binding.cvIncomingCall, isOn)
        toggleCardState(binding.cvNotificationSms, isOn)
        toggleCardState(binding.cvFlashSettings, isOn)
    }

    fun isNotificationAccessGranted(context: Context): Boolean {
        val enabledListeners = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        return !TextUtils.isEmpty(enabledListeners) && enabledListeners.contains(context.packageName)
    }

}
