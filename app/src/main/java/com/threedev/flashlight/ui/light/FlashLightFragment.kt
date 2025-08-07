package com.threedev.flashlight.ui.light

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.threedev.flashlight.MainActivity
import com.threedev.flashlight.R
import com.threedev.flashlight.databinding.FragmentFlashlightBinding
import com.threedev.flashlight.helper.FlashLightManager
import com.threedev.flashlight.helper.SessionManager
import com.threedev.flashlight.ui.CameraActivity
import com.threedev.flashlight.ui.FlashTypeBottomSheet

class FlashLightFragment : Fragment() {

    private lateinit var binding: FragmentFlashlightBinding
    private var isFlashOn = false
    private var isRhythmMode = false

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFlashlightBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Load the last selected mode from SessionManager
        isRhythmMode = SessionManager.getBool(SessionManager.FLASH_MODE_RHYTHM, false)

        binding.ivOnLight.setOnClickListener {
            toggleFlashLight()
        }

        binding.shortcutButton.setOnClickListener {
            createShortcut()
        }

        binding.cameraButton.setOnClickListener {
            val intent = Intent(requireContext(), CameraActivity::class.java)
            startActivity(intent)
        }

        binding.root.setOnClickListener {
            findNavController().navigate(R.id.action_Flash_light_to_screenlight)
        }

        binding.modeButton.setOnClickListener {
            showFlashTypeBottomSheet()
        }

        return root
    }

    private fun toggleFlashLight() {
        if (isFlashOn) {
            FlashLightManager.stopBlinking()
            FlashLightManager.openFlashLight(requireContext(), false)
            isFlashOn = false
        } else {

            isFlashOn = true
            if (isRhythmMode) {
                FlashLightManager.startBlinking(requireContext(), 500)
            } else {
                FlashLightManager.openFlashLight(requireContext(), true)
            }
        }
    }


    private fun showFlashTypeBottomSheet() {
        val bottomSheet = FlashTypeBottomSheet(requireContext(), R.style.TransparentBottomSheetDialog) { flashType ->
            when (flashType) {
                "Continuous" -> {
                    isRhythmMode = false
                    SessionManager.putBool(SessionManager.FLASH_MODE_RHYTHM, false)
                }
                "Rhythm" -> {
                    isRhythmMode = true
                    SessionManager.putBool(SessionManager.FLASH_MODE_RHYTHM, true)
                }
            }
        }
        bottomSheet.show(parentFragmentManager, "FlashTypeBottomSheet")
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun createShortcut() {
        val shortcutManager = requireContext().getSystemService(ShortcutManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (shortcutManager!!.isRequestPinShortcutSupported) {
                val shortcut = ShortcutInfo.Builder(requireContext(), "flashlight_shortcut")
                    .setShortLabel("Flashlight")
                    .setLongLabel("Open Flashlight")
                    .setIcon(Icon.createWithResource(requireContext(), R.drawable.splash_icone))
                    .setIntent(Intent(requireContext(), MainActivity::class.java).setAction(Intent.ACTION_VIEW))
                    .build()

                val success = shortcutManager.requestPinShortcut(shortcut, null)
                Toast.makeText(requireContext(), if (success) "Shortcut Added!" else "Shortcut not supported", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Shortcut feature not supported on this device", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Requires Android 8.0+", Toast.LENGTH_SHORT).show()
        }
    }
}

