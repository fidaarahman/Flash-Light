package com.threedev.flashlight.ui

import Language
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.threedev.flashlight.R
import com.threedev.flashlight.adapters.LanguageAdapter
import com.threedev.flashlight.databinding.ActivityLangaugeBinding
import com.threedev.flashlight.helper.SessionManager
import com.threedev.flashlight.ui.onbaording.OnBoardActivity
import java.util.*

class LangaugeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLangaugeBinding
    private lateinit var languages: List<Language>
    private var selectedLanguageCode: String = "en"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLangaugeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isFirstLaunch = SessionManager.getBool(SessionManager.KEY_IS_FIRST_LAUNCH, true)

        if (!isFirstLaunch) {

            startActivity(Intent(this, OnBoardActivity::class.java))
            finish()
        } else {
            SessionManager.putBool(SessionManager.KEY_IS_FIRST_LAUNCH, false)
        }


        languages = listOf(
            Language("English", R.drawable.english, "en"),
            Language("Arabic", R.drawable.arabic, "ar"),
            Language("Spanish", R.drawable.spanish_icone, "es"),
            Language("Portuguese", R.drawable.portogal, "pt"),
            Language("German", R.drawable.german, "de"),
            Language("French", R.drawable.french, "fr"),
            Language("Urdu", R.drawable.urdu, "ur"),
            Language("Hindi", R.drawable.hindi, "hi")
        )

        binding.rvLanguageList.layoutManager = LinearLayoutManager(this)
        binding.rvLanguageList  .adapter = LanguageAdapter(languages) { selectedLanguage ->
            selectedLanguageCode = selectedLanguage.localeCode
        }

        binding.llApplyButton.setOnClickListener {
            updateLocale(selectedLanguageCode)
            SessionManager.setString(SessionManager.KEY_LOCALE_LANGUAGE, selectedLanguageCode)
            startActivity(Intent(this, OnBoardActivity::class.java))
            finish()
        }
    }

    private fun updateLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}

