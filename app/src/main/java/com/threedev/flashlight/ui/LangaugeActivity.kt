package com.threedev.flashlight.ui

import Language
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        val savedLanguageCode = SessionManager.getString(SessionManager.KEY_LOCALE_LANGUAGE, "en")

        binding = ActivityLangaugeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 👇 Only skip if first launch flag is false
        val isFirstLaunch = SessionManager.getBool(SessionManager.KEY_IS_FIRST_LAUNCH, true)
        if (!isFirstLaunch) {
            startActivity(Intent(this, OnBoardActivity::class.java))
            finish()
            return
        }


        languages = listOf(
            Language("English", R.drawable.english, "en"),
            Language("Arabic", R.drawable.arabic, "ar"),
            Language("Portuguese", R.drawable.portogal, "pt"),
            Language("German", R.drawable.german, "de"),
            Language("French", R.drawable.french, "fr"),
            Language("Urdu", R.drawable.urdu, "ur"),
            Language("Hindi", R.drawable.hindi, "hi")
        )

        binding.rvLanguageList.layoutManager = LinearLayoutManager(this)
        binding.rvLanguageList.adapter = LanguageAdapter(languages) { selectedLanguage ->
            selectedLanguageCode = selectedLanguage.localeCode
        }

        binding.llApplyButton.setOnClickListener {
            updateLocale(selectedLanguageCode)
            SessionManager.setString(SessionManager.KEY_LOCALE_LANGUAGE, selectedLanguageCode)
            SessionManager.putBool(SessionManager.KEY_IS_FIRST_LAUNCH, false)

            val intent = Intent(this, OnBoardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }


    // Function to update locale based on selected language code
    private fun updateLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}
