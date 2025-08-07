package com.threedev.flashlight.helper

import android.app.Application
<<<<<<< HEAD
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale
=======
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889

class AppController: Application() {

    override fun onCreate() {
        super.onCreate()
        SessionManager.with(this)
<<<<<<< HEAD
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
=======
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
    }
}