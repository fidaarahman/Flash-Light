package com.threedev.flashlight.helper

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

class AppController: Application() {

    override fun onCreate() {
        super.onCreate()
        SessionManager.with(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }


    companion object {
        fun setAppLocale(context: Context): Context {
            val langCode = SessionManager.getString(SessionManager.KEY_LOCALE_LANGUAGE, "en")
            val locale = Locale(langCode)
            Locale.setDefault(locale)

            val config = Configuration()
            config.setLocale(locale)
            config.setLayoutDirection(locale)

            return context.createConfigurationContext(config)
        }
    }
}