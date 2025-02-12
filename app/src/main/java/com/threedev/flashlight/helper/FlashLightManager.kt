package com.threedev.flashlight.helper

import android.content.Context
import android.hardware.Camera
import android.hardware.camera2.CameraManager
import android.os.Build
import kotlinx.coroutines.*

object FlashLightManager {

    private var camera: Camera? = null
    private var isBlinking = false
    private var blinkJob: Job? = null
    private var mCameraManager: CameraManager? = null
    private var mCameraId: String? = null
    private var isFlashLightOn = false

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
                toggleFlashlight(context)
                delay(speed)
                toggleFlashlight(context)
                delay(speed)
            }
        }
    }

    fun stopBlinking(context: Context) {
        isBlinking = false
        blinkJob?.cancel()
        if (isFlashLightOn) {
            isFlashLightOn = false
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                handleLegacyCamera(false)
            } else {
                mCameraId?.let {
                    mCameraManager?.setTorchMode(it, false)
                }
            }
        }
    }

    fun offFlashlight(context: Context) {
        isBlinking = false
        blinkJob?.cancel()

        camera?.stopPreview()
        camera?.release()
        camera = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                mCameraId?.let {
                    mCameraManager?.setTorchMode(it, false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
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