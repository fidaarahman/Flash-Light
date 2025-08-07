package com.threedev.flashlight.ui

import Language
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
<<<<<<< HEAD
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
=======
import androidx.appcompat.app.AppCompatActivity
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
import androidx.recyclerview.widget.LinearLayoutManager
import com.threedev.flashlight.MainActivity
import com.threedev.flashlight.R
import com.threedev.flashlight.adapters.SettingLangaugeAdapter
import com.threedev.flashlight.databinding.ActivitySettingLanguageBinding
<<<<<<< HEAD
import com.threedev.flashlight.helper.AppController
=======
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
import com.threedev.flashlight.helper.LocaleHelper


class SettingLanguageActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingLanguageBinding
    private lateinit var languages: List<Language>
    private var selectedLanguageCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
<<<<<<< HEAD
        enableEdgeToEdge()
        binding = ActivitySettingLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

=======

        binding = ActivitySettingLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Define the list of languages
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
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

        // Setup RecyclerView to display languages
        binding.languageRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.languageRecyclerView.adapter = SettingLangaugeAdapter(languages) { selectedLanguage ->
            selectedLanguageCode = selectedLanguage.localeCode

            // Show the Apply button when a language is selected
            binding.llApplyButton.visibility = View.VISIBLE
        }

        // "Apply" button logic
        binding.llApplyButton.setOnClickListener {
            if (selectedLanguageCode != null) {
                applyLanguage(selectedLanguageCode!!)
            } else {
                Toast.makeText(this, "Please select a language first", Toast.LENGTH_SHORT).show()
            }
        }

        // Skip button logic
        binding.imgSkip.setOnClickListener {
            // Default to English if skipped
            applyLanguage("en")
        }
    }

<<<<<<< HEAD
=======
    // Apply the selected language
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
    private fun applyLanguage(languageCode: String) {
        // Update app locale
        LocaleHelper.updateLocaleLanguage(this, languageCode)

        // Save the selected language preference
        saveLanguagePreference(languageCode)

        // Show confirmation toast
        Toast.makeText(this, "Language changed successfully", Toast.LENGTH_SHORT).show()

        // Navigate to MainActivity
        navigateToMainActivity()
    }

<<<<<<< HEAD
=======
    // Save the selected language in SharedPreferences
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
    private fun saveLanguagePreference(languageCode: String) {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("language_code", languageCode).apply()
    }

<<<<<<< HEAD
=======
    // Navigate to MainActivity
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
    private fun navigateToMainActivity() {
        startActivity(
            Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
        finish()
    }
<<<<<<< HEAD
    override fun attachBaseContext(newBase: Context) {
        val context = AppController.setAppLocale(newBase)
        super.attachBaseContext(context)
    }
=======
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889

}
