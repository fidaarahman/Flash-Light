package com.threedev.flashlight.ui.home

import android.Manifest
<<<<<<< HEAD
import android.content.Context
import android.content.Intent
=======
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
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
<<<<<<< HEAD
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
=======
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.app.ActivityCompat.getColor
import androidx.core.content.ContextCompat
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
<<<<<<< HEAD
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.threedev.flashlight.R
import com.threedev.flashlight.databinding.FragmentHomeBinding
import com.threedev.flashlight.helper.FlashLightManager
import com.threedev.flashlight.helper.SessionManager
=======
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.threedev.flashlight.R
import com.threedev.flashlight.databinding.FragmentHomeBinding
import com.threedev.flashlight.helper.CallReciever
import com.threedev.flashlight.helper.FlashLightManager
import com.threedev.flashlight.helper.NotificationService
import com.threedev.flashlight.helper.SessionManager
import com.threedev.flashlight.helper.SmsReciver
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
import com.threedev.flashlight.ui.FlashTypeBottomSheet
import com.threedev.flashlight.ui.MessageDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
<<<<<<< HEAD
    private var isBlinking = false
    private var blinkspeed: Long = 300
=======
    private var isBlinking=false
    private var blinkspeed : Long=300
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
    private var isRhythmMode = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
<<<<<<< HEAD

        return binding.root
=======
        val root: View = binding.root


        return root
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

<<<<<<< HEAD
        if (SessionManager.getBool(SessionManager.FLASH_MODE_RHYTHM, true)) {
            binding.tvFlashType.text = resources.getText(R.string.continuous)
        } else {
            binding.tvFlashType.text = resources.getText(R.string.rhythm)
        }

        binding.sliderFlashSpeed.addOnChangeListener { _, value, _ ->
            blinkspeed = value.toLong().coerceAtLeast(50)
            binding.tvSpeed.text = "$blinkspeed ms"
            SessionManager.setInt(SessionManager.KEY_FLASH_BLINK_SPEED, blinkspeed.toInt())
=======
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
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889

            Log.d("SliderFlashSpeed", "Blink speed updated: $blinkspeed ms")

            if (isBlinking) {
<<<<<<< HEAD
                FlashLightManager.stopBlinking()
                CoroutineScope(Dispatchers.Main).launch {
                    delay(blinkspeed)
=======
                FlashLightManager.stopBlinking(requireContext())
                CoroutineScope(Dispatchers.Main).launch {
                    delay(100)
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
                    FlashLightManager.startBlinking(requireContext(), blinkspeed)
                }
            }
        }

        binding.llFlashType.setOnClickListener {
            showFlashTypeBottomSheet()
        }
<<<<<<< HEAD

=======
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
        binding.btnTest.setOnClickListener {
            FlashLightManager.startBlinking(requireContext(), blinkspeed)

            CoroutineScope(Dispatchers.Main).launch {
                delay(3000)
<<<<<<< HEAD
                FlashLightManager.stopBlinking()
            }
        }

        binding.mainSwitch.setOnToggledListener { _, isChecked ->
            SessionManager.putBool(SessionManager.MAIN_TOGGLE, isChecked)
            initializeCardStates(binding.mainSwitch.isOn)

            /*if (binding.mainSwitch.isOn){
                if (!isNotificationAccessGranted(requireContext())) {
                    MessageDialogBuilder.Builder(requireActivity())
                        .withTitle("Notification permission")
                        .withMessage("We require notification access to detect incoming calls and SMS for functionality purposes only. We do not collect, store, or share any personal data from your notifications.")
                        .withOkButtonListener("OK", object : MessageDialogBuilder.OnOkClick {
                            override fun onClick(dialogs: androidx.appcompat.app.AlertDialog) {
                                dialogs.dismiss()
                                val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                                startActivity(intent)
                            }
                        }).build()
                    SessionManager.putBool(SessionManager.MAIN_TOGGLE, false)
                    binding.mainSwitch.isOn = false
                    return@setOnToggledListener
                }else{
                    SessionManager.putBool(SessionManager.MAIN_TOGGLE, isChecked)
                }
            }else{
                SessionManager.putBool(SessionManager.MAIN_TOGGLE, isChecked)
            }*/

            /*val hasAllPermissions =
=======
                FlashLightManager.stopBlinking(requireContext())
            }
        }

        binding.mainToggle.setOnToggledListener { _, isChecked ->
            val hasAllPermissions = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
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
<<<<<<< HEAD
=======
                        Manifest.permission.CAMERA,
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
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
<<<<<<< HEAD
            }*/

        }

        binding.switchIncomingCall.setOnToggledListener { _, isChecked ->
            if (isChecked) {
                Dexter.withContext(requireContext())
                    .withPermissions(Manifest.permission.READ_PHONE_STATE)
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                            if (report.areAllPermissionsGranted()) {
                                SessionManager.putBool(
                                    SessionManager.INCOMING_CALL_TOGGLE_STATE,
                                    isChecked
                                )


                            } else if (report.isAnyPermissionPermanentlyDenied) {
                                SessionManager.putBool(
                                    SessionManager.INCOMING_CALL_TOGGLE_STATE,
                                    false
                                )
                                binding.switchIncomingCall.isOn = false
                                MessageDialogBuilder.Builder(requireActivity())
                                    .withTitle("Permission Denied Permanently")
                                    .withMessage("Go to settings and give permission.")
                                    .withOkButtonListener(
                                        "Go to Settings",
                                        object : MessageDialogBuilder.OnOkClick {
                                            override fun onClick(dialogs: androidx.appcompat.app.AlertDialog) {
                                                dialogs.dismiss()
                                                val intent =
                                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                                val uri = Uri.fromParts(
                                                    "package",
                                                    requireContext().packageName,
                                                    null
                                                )
                                                intent.setData(uri)
                                                startActivity(intent)
                                            }
                                        }).build()
                            } else {
                                SessionManager.putBool(
                                    SessionManager.INCOMING_CALL_TOGGLE_STATE,
                                    false
                                )
                                binding.switchIncomingCall.isOn = false
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permissions: MutableList<PermissionRequest>,
                            token: PermissionToken
                        ) {
                            token.continuePermissionRequest()
                        }
                    })
                    .check()
            } else {
                SessionManager.putBool(SessionManager.INCOMING_CALL_TOGGLE_STATE, false)
            }
        }

        binding.switchNotificationSms.setOnToggledListener { _, isChecked ->
            if (isChecked) {
                if (isNotificationServiceEnabled(requireContext())) {
                    SessionManager.putBool(SessionManager.INCOMING_SMS, true)
                    Log.d("Permissions", "Notification Listener permission granted")
                } else {
                    // Show dialog explaining why access is needed before opening settings
                    AlertDialog.Builder(requireContext())
                        .setTitle("Permission Required")
                        .setMessage("To enable this feature, please allow notification access in settings.")
                        .setPositiveButton("Go to Settings") { dialog, _ ->
                            dialog.dismiss()
                            openNotificationAccessSettings(requireContext())
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                            binding.switchNotificationSms.setOn(false)
                            SessionManager.putBool(SessionManager.INCOMING_SMS, false)
                        }
                        .setCancelable(false)
                        .show()
                }
            } else {
                SessionManager.putBool(SessionManager.INCOMING_SMS, false)
                Log.d("SMSPermission", "Notification-based feature disabled")
            }
        }


        binding.llSelectApp.setOnClickListener {
            if (binding.mainSwitch.isOn && binding.switchNotificationSms.isOn) {
                if (isNotificationAccessGranted(requireContext())) {
                    findNavController().navigate(R.id.action_Home_to_Moreapps)
                    Log.d("Notification_permission", "Permission for Notification is granted")
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please enable notification access for app selection.",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                    startActivity(intent)
                    Log.d("Notification_permission", "Permission is not granted")
                }
            }
        }

    }
    private fun isNotificationServiceEnabled(context: Context): Boolean {
        val pkgName = context.packageName
        val flat = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        Log.d("PermissionCheck", "enabled_notification_listeners: $flat")
        return flat?.split(":")?.any { it.contains(pkgName) } == true
    }
=======
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

>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
    private fun showFlashTypeBottomSheet() {
        val bottomSheet = FlashTypeBottomSheet(requireContext(), R.style.TransparentBottomSheetDialog) { flashType ->
            when (flashType) {
                "Continuous" -> {
                    isRhythmMode = false
<<<<<<< HEAD
                    SessionManager.putBool(SessionManager.FLASH_MODE_RHYTHM, false)
                    binding.tvFlashType.setText("Continuous")
                }
                "Rhythm" -> {
                    isRhythmMode = true
                    SessionManager.putBool(SessionManager.FLASH_MODE_RHYTHM, true)
                    binding.tvFlashType.text = "Rhythm"
=======
                    SessionManager.putBool("FLASH_MODE_RHYTHM", false)
                }
                "Rhythm" -> {
                    isRhythmMode = true
                    SessionManager.putBool("FLASH_MODE_RHYTHM", true)
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
                }
            }
        }
        bottomSheet.show(parentFragmentManager, "FlashTypeBottomSheet")
    }

<<<<<<< HEAD
    private fun initializeCardStates(isOn: Boolean) {
        val callingState = SessionManager.getBool(SessionManager.INCOMING_CALL_TOGGLE_STATE, false)
        val notificationState = SessionManager.getBool(SessionManager.INCOMING_SMS, false)
        if (!isOn){
            binding.cvIncomingCall.isEnabled = false
            binding.cvNotificationSms.isEnabled = false
            binding.cvIncomingCall.alpha = 0.5f
            binding.cvNotificationSms.alpha = 0.5f
            binding.switchIncomingCall.isEnabled = false
            binding.switchNotificationSms.isEnabled = false
            binding.switchIncomingCall.isOn = false
            binding.switchNotificationSms.isOn = false
        }else{
            binding.cvIncomingCall.isEnabled = true
            binding.cvNotificationSms.isEnabled = true
            binding.cvIncomingCall.alpha = 1f
            binding.cvNotificationSms.alpha = 1f
            binding.switchIncomingCall.isEnabled = true
            binding.switchNotificationSms.isEnabled = true
            binding.switchIncomingCall.isOn = callingState
            binding.switchNotificationSms.isOn = notificationState
            binding.switchIncomingCall.colorBorder = ResourcesCompat.getColor(resources, R.color.colorPink, null)
            binding.switchNotificationSms.colorBorder = ResourcesCompat.getColor(resources, R.color.colorPink, null)
        }
    }

    private fun isNotificationAccessGranted(context: Context): Boolean {
        val enabledListeners = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        return !TextUtils.isEmpty(enabledListeners) && enabledListeners.contains(context.packageName)
    }
    fun openNotificationAccessSettings(context: Context) {
        val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        val isMainToggleOn = SessionManager.getBool(SessionManager.MAIN_TOGGLE, false)
        binding.mainSwitch.isOn = isMainToggleOn
        initializeCardStates(isMainToggleOn)

        val notificationAccessGranted = isNotificationServiceEnabled(requireContext())
        val smsPermissionEnabled = SessionManager.getBool(SessionManager.INCOMING_SMS, false)

        if (notificationAccessGranted && !smsPermissionEnabled) {
            Log.d("NotificationAccess", "Access now granted. Enabling switch_notification_sms.")
            SessionManager.putBool(SessionManager.INCOMING_SMS, true)
            binding.switchNotificationSms.setOn(true)
        }

        val hasCallPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED

        val callToggleShouldBeOn = SessionManager.getBool(SessionManager.INCOMING_CALL_TOGGLE_STATE, false)

        if (hasCallPermission && callToggleShouldBeOn) {
            binding.switchIncomingCall.setOn(true)
        } else if (!hasCallPermission) {
            SessionManager.putBool(SessionManager.INCOMING_CALL_TOGGLE_STATE, false)
            binding.switchIncomingCall.setOn(false)
        }
    }

=======
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
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889

}
