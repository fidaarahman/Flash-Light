package com.threedev.flashlight.helper

import android.content.Context
import android.hardware.Camera
import android.hardware.camera2.CameraManager
<<<<<<< HEAD
=======
import android.os.Build
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
import kotlinx.coroutines.*

object FlashLightManager {

    private var camera: Camera? = null
    private var isBlinking = false
    private var blinkJob: Job? = null
    private var mCameraManager: CameraManager? = null
    private var mCameraId: String? = null
    private var isFlashLightOn = false

<<<<<<< HEAD
    fun openFlashLight(context: Context, isChecked: Boolean) {
        handleCamera2(context, isChecked)
    }

    private fun toggleFlashlight(context: Context) {
        isFlashLightOn = !isFlashLightOn
        handleCamera2(context, isFlashLightOn)
=======
    fun OpenFlashLight(context: Context, isChecked: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            handleLegacyCamera(isChecked)
        } else {
            handleCamera2(context, isChecked)
        }
    }

    fun toggleFlashlight(context: Context) {
        isFlashLightOn = !isFlashLightOn
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            handleLegacyCamera(isFlashLightOn)
        } else {
            handleCamera2(context, isFlashLightOn)
        }
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
    }

    private fun handleLegacyCamera(isChecked: Boolean) {
        try {
            if (isChecked) {
                if (camera == null) {
                    camera = Camera.open()
                }
                val parameters = camera?.parameters
                parameters?.flashMode = Camera.Parameters.FLASH_MODE_TORCH
                camera?.parameters = parameters
                camera?.startPreview()
            } else {
                camera?.stopPreview()
                camera?.release()
                camera = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleCamera2(context: Context, isChecked: Boolean) {
        try {
            if (mCameraManager == null) {
                mCameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                mCameraId = mCameraManager?.cameraIdList?.get(0)
            }

            mCameraId?.let {
                mCameraManager?.setTorchMode(it, isChecked)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startBlinking(context: Context, speed:Long) {
        if (isBlinking) return
        isBlinking = true
        blinkJob = CoroutineScope(Dispatchers.Default).launch {
            while (isBlinking) {
<<<<<<< HEAD
                if (SessionManager.getBool(SessionManager.FLASH_MODE_RHYTHM, true)) {
                    toggleFlashlight(context)
                    delay(speed)
                    toggleFlashlight(context)
                    delay(speed)
                }else{
                    toggleFlashlight(context)
                }
=======
                toggleFlashlight(context)
                delay(speed)
                toggleFlashlight(context)
                delay(speed)
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
            }
        }
    }

<<<<<<< HEAD
    fun stopBlinking() {
=======
    fun stopBlinking(context: Context) {
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
        isBlinking = false
        blinkJob?.cancel()
        if (isFlashLightOn) {
            isFlashLightOn = false
<<<<<<< HEAD
            mCameraId?.let {
                mCameraManager?.setTorchMode(it, false)
=======
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                handleLegacyCamera(false)
            } else {
                mCameraId?.let {
                    mCameraManager?.setTorchMode(it, false)
                }
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
            }
        }
    }

    fun offFlashlight(context: Context) {
        isBlinking = false
        blinkJob?.cancel()

        camera?.stopPreview()
        camera?.release()
        camera = null

<<<<<<< HEAD
        try {
            mCameraId?.let {
                mCameraManager?.setTorchMode(it, false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
=======
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                mCameraId?.let {
                    mCameraManager?.setTorchMode(it, false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
        }
    }

    fun flashOnce(context: Context,speed:Long) {
        CoroutineScope(Dispatchers.Default).launch {
            toggleFlashlight(context)
            delay(200)
            toggleFlashlight(context)
        }
    }


}