package com.threedev.flashlight.helper

import android.app.Application

class AppController: Application() {

    override fun onCreate() {
        super.onCreate()
        SessionManager.with(this)
    }
}