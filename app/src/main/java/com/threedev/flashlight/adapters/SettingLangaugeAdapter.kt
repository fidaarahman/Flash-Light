package com.threedev.flashlight.adapters

import Language
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.threedev.flashlight.databinding.ItemLangaugeCardBinding
import com.threedev.flashlight.helper.SessionManager

class SettingLangaugeAdapter(
    private val languages: List<Language>,
    private val onLanguageClick: (Language) -> Unit
) : RecyclerView.Adapter<SettingLangaugeAdapter.LanguageViewHolder>() {

    // Set the default selected position from saved language preference
    private var selectedPosition = languages.indexOfFirst {
        it.localeCode == SessionManager.getString(SessionManager.KEY_LOCALE_LANGUAGE, "en")
    }

    inner class LanguageViewHolder(val binding: ItemLangaugeCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding = ItemLangaugeCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LanguageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val language = languages[position]
        val binding = holder.binding

        // Set language name and flag
        binding.tvLanguageName.text = language.name
        binding.ivLanguageFlag.setImageResource(language.flagResId)

        // Highlight the selected language
        if (position == selectedPosition) {
            binding.tvLanguageName.setTextColor(ContextCompat.getColor(binding.root.context, android.R.color.black))
            binding.cardLanguage.isSelected = true
        } else {
            binding.tvLanguageName.setTextColor(ContextCompat.getColor(binding.root.context, android.R.color.white))
            binding.cardLanguage.isSelected = false
        }

        // Handle language selection
        binding.root.setOnClickListener {
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()
            onLanguageClick(language)
        }

        // Ensure the selection effect is applied properly
        binding.root.isSelected = position == selectedPosition
    }

    override fun getItemCount(): Int = languages.size
}
