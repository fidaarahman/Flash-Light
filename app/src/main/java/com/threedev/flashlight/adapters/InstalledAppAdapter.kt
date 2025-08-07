package com.threedev.flashlight.adapters

import android.content.pm.ApplicationInfo
<<<<<<< HEAD
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.threedev.flashlight.databinding.ItemInstalledAppBinding
import com.threedev.flashlight.helper.SessionManager
=======
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.threedev.flashlight.databinding.ItemInstalledAppBinding
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889

class InstalledAppAdapter(
    private val appsList: List<ApplicationInfo>,
    private val onAppSelected: (ApplicationInfo) -> Unit
) : RecyclerView.Adapter<InstalledAppAdapter.AppViewHolder>() {

<<<<<<< HEAD

=======
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = ItemInstalledAppBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AppViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
<<<<<<< HEAD
        val info = appsList[position]
        holder.binding.rbSelectApp.isChecked = if (SessionManager.getSets(SessionManager.SELECT_APP).contains(appsList[position].packageName)){
            true
        }else{
            false
        }

        val pm = holder.binding.ivAppIcon.context.packageManager
        // Set app name
        holder.binding.tvAppName.text = info.loadLabel(pm).toString()

        Glide.with(holder.binding.ivAppIcon.context)
            .load(info.loadIcon(pm))
            .into(holder.binding.ivAppIcon)

        holder.binding.rbSelectApp.setOnClickListener {
            onAppSelected(info)
        }
        /*holder.binding.rbSelectApp.setOnCheckedChangeListener { _, isChecked ->
            Log.d(" OnChange", "On Checked Chage = > ${info.packageName}:$isChecked")
            onAppSelected(info)
        }*/
=======
        holder.bind(appsList[position])
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
    }

    override fun getItemCount(): Int = appsList.size

<<<<<<< HEAD
    inner class AppViewHolder(val binding: ItemInstalledAppBinding) :
        RecyclerView.ViewHolder(binding.root) {

=======
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
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
    }
}
