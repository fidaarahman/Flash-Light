package com.threedev.flashlight.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.threedev.flashlight.R

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var progressBar: LinearProgressIndicator
    private val progressMax = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        progressBar = findViewById(R.id.splash_progress_bar)
        progressBar.setIndicatorColor(getColor(R.color.text_yellow))
        progressBar.max = progressMax

        //activateRemoteConfig()
        hideSystemUI()
        startProgressUpdate()
//
//        if (SessionManager.getInt(SessionManager.KEY_SCANNED_LIMIT, -5) == -5) {
//            SessionManager.putInt(SessionManager.KEY_SCANNED_LIMIT, 5)
//        }
    }

    private fun hideSystemUI() {
        window.decorView.apply {
            systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    private fun startProgressUpdate() {
        val handler = Handler(Looper.getMainLooper())
        var progress = 0

        val totalDuration = 5000L
        val updateInterval = 50L
        val incrementValue = (progressMax * updateInterval / totalDuration).toInt()

        val runnable = object : Runnable {
            override fun run() {
                progressBar.progress = progress
                if (progress >= progressMax) {
                    startActivity(Intent(this@SplashScreenActivity, LangaugeActivity::class.java))
                    finish()
                } else {
                    progress += incrementValue
                    handler.postDelayed(this, updateInterval)
                }
            }
        }
        handler.post(runnable)
    }

//    private fun activateRemoteConfig() {
//        val remoteConfig = FirebaseRemoteConfig.getInstance()
//        val configSettings = FirebaseRemoteConfigSettings.Builder()
//            .setMinimumFetchIntervalInSeconds(3600)
//            .build()
//        remoteConfig.setConfigSettingsAsync(configSettings)
//        remoteConfig.setDefaultsAsync(R.xml.remote_config_banner_defaults)
//        remoteConfig.fetchAndActivate().addOnCompleteListener(
//            this
//        ) { task: Task<Boolean?> ->
//            if (task.isSuccessful) {
//                val activeValue = remoteConfig.getValue("banner_ad")
//                val activeValue1 =
//                    remoteConfig.getValue("interstitial_ad")
//                val activeValue2 = remoteConfig.getValue("native_ad")
//                Log.d(
//                    "remote_config",
//                    "Config params updated: " + activeValue.asBoolean()
//                )
//                Log.d(
//                    "remote_config",
//                    "Config params updated: " + activeValue1.asBoolean()
//                )
//                Log.d(
//                    "remote_config",
//                    "Config params updated: " + activeValue2.asBoolean()
//                )
//            } else {
//                Log.d("remote_config", "Config params updated: Error")
//            }
//        }
//    }

}
