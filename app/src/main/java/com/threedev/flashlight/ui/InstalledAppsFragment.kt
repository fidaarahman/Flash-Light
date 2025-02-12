package com.threedev.flashlight.ui

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.threedev.flashlight.adapters.InstalledAppAdapter
import com.threedev.flashlight.databinding.FragmentInstalledFragmentBinding
import com.threedev.flashlight.helper.FlashLightManager
import com.threedev.flashlight.helper.SessionManager

class InstalledAppsFragment : Fragment() {

    private lateinit var binding: FragmentInstalledFragmentBinding
    private lateinit var adapter: InstalledAppAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInstalledFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val installedApps = getInstalledApps()

        val installedAppsAdapter = InstalledAppAdapter(installedApps) { selectedApp ->
            handleAppSelection(selectedApp)
        }

        binding.recyclerViewInstalledApps.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = installedAppsAdapter
        }

    }

    private fun getInstalledApps(): List<ApplicationInfo> {
        val packageManager = requireContext().packageManager
        val allApps = packageManager.getInstalledApplications(PackageManager.MATCH_ALL)

        for (appInfo in allApps) {
            Log.d("InstalledAppsFragment", "Installed package: ${appInfo.packageName}")
            Log.d("InstalledAppsFragment", "Source dir: ${appInfo.sourceDir}")
            Log.d(
                "InstalledAppsFragment",
                "Launch Activity: ${packageManager.getLaunchIntentForPackage(appInfo.packageName)}"
            )
        }

        return allApps.filter { app ->
            (app.flags and ApplicationInfo.FLAG_SYSTEM) == 0 &&
                    (app.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0
        }
    }

    private fun handleAppSelection(appInfo: ApplicationInfo) {
        val packageManager = requireContext().packageManager
        val appName = appInfo.loadLabel(packageManager).toString()
        val packageName = appInfo.packageName
        SessionManager.setString(SessionManager.SELECT_APP,packageName)
        val sharedPreferences = requireContext().getSharedPreferences("FlashlightPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("SELECTED_APP", packageName).apply()

        // Log the selected app
        Log.d("InstalledAppsFragment", "Selected app saved: $appName ($packageName)")

    }
}
