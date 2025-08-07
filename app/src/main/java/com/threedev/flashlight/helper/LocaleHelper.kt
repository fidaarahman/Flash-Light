package com.threedev.flashlight.helper

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.util.Log
import java.util.Locale

object LocaleHelper {

    fun setLocaleLanguage(context: Context){
        val localLang = SessionManager.getString(SessionManager.KEY_LOCALE_LANGUAGE, "ur")
        Log.d("Selected Lang", "$localLang")
        val locale = Locale(localLang)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            val localManager = context.getSystemService(LocaleManager::class.java)
            localManager.applicationLocales = LocaleList.forLanguageTags(localLang)
        }

    }

    fun updateLocaleLanguage(context: Context, localeLangCode:String){
        SessionManager.setString(SessionManager.KEY_LOCALE_LANGUAGE, localeLangCode)
        setLocaleLanguage(context)
    }



}