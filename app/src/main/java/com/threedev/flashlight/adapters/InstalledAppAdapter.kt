package com.threedev.flashlight.adapters

import android.content.pm.ApplicationInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.threedev.flashlight.databinding.ItemInstalledAppBinding

class InstalledAppAdapter(
    private val appsList: List<ApplicationInfo>,
    private val onAppSelected: (ApplicationInfo) -> Unit
) : RecyclerView.Adapter<InstalledAppAdapter.AppViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = ItemInstalledAppBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AppViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(appsList[position])
    }

    override fun getItemCount(): Int = appsList.size

    inner class AppViewHolder(private val binding: ItemInstalledAppBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(appInfo: ApplicationInfo) {
            val packageManager = binding.root.context.packageManager

            // Set app name
            binding.tvAppName.text = appInfo.loadLabel(packageManager).toString()

            // Set app icon
            binding.ivAppIcon.setImageDrawable(appInfo.loadIcon(packageManager))


            binding.rbSelectApp.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    onAppSelected(appInfo)
                }
            }
        }
    }
}
