package com.threedev.flashlight.ui

<<<<<<< HEAD
import android.app.ProgressDialog
=======
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
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
<<<<<<< HEAD
import com.threedev.flashlight.helper.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
=======
import com.threedev.flashlight.helper.FlashLightManager
import com.threedev.flashlight.helper.SessionManager
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889

class InstalledAppsFragment : Fragment() {

    private lateinit var binding: FragmentInstalledFragmentBinding
<<<<<<< HEAD
    private lateinit var installAppsAdapter: InstalledAppAdapter
    private val installedApps:MutableList<ApplicationInfo> = ArrayList()
    private val progressDialog by lazy {
        ProgressDialog(requireContext())
    }
=======
    private lateinit var adapter: InstalledAppAdapter
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889

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

<<<<<<< HEAD
        progressDialog.setMessage("Fetching installed apps")
        progressDialog.show()

        installAppsAdapter = InstalledAppAdapter(installedApps) { selectedApp ->
            //handleAppSelection(selectedApp)
            //val packageName = selectedApp.packageName
            val selectedApps = SessionManager.getSets(SessionManager.SELECT_APP)
            if (selectedApps.contains(selectedApp.packageName)){
                selectedApps.remove(selectedApp.packageName)
                SessionManager.putSets(SessionManager.SELECT_APP, selectedApps)
            }else{
                selectedApps.add(selectedApp.packageName)
                SessionManager.putSets(SessionManager.SELECT_APP, selectedApps)
            }
            //SessionManager.putSets(SessionManager.SELECT_APP, packageName)
        }

        CoroutineScope(Dispatchers.IO).launch {
            getInstalledApps()
        }


        binding.recyclerViewInstalledApps.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = installAppsAdapter
=======

        val installedApps = getInstalledApps()

        val installedAppsAdapter = InstalledAppAdapter(installedApps) { selectedApp ->
            handleAppSelection(selectedApp)
        }

        binding.recyclerViewInstalledApps.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = installedAppsAdapter
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
        }

    }

<<<<<<< HEAD
    private suspend fun getInstalledApps() {
        val packageManager = requireContext().packageManager
        val allApps = packageManager.getInstalledApplications(PackageManager.MATCH_ALL)

        /*for (appInfo in allApps) {
=======
    private fun getInstalledApps(): List<ApplicationInfo> {
        val packageManager = requireContext().packageManager
        val allApps = packageManager.getInstalledApplications(PackageManager.MATCH_ALL)

        for (appInfo in allApps) {
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
            Log.d("InstalledAppsFragment", "Installed package: ${appInfo.packageName}")
            Log.d("InstalledAppsFragment", "Source dir: ${appInfo.sourceDir}")
            Log.d(
                "InstalledAppsFragment",
                "Launch Activity: ${packageManager.getLaunchIntentForPackage(appInfo.packageName)}"
            )
<<<<<<< HEAD
        }*/

        val filteredApps = allApps.filter { app ->
            (app.flags and ApplicationInfo.FLAG_SYSTEM) == 0 &&
                    (app.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0
        }

        withContext(Dispatchers.Main){
            installedApps.addAll(filteredApps)
            installAppsAdapter.notifyDataSetChanged()
            progressDialog.dismiss()
        }

=======
        }

        return allApps.filter { app ->
            (app.flags and ApplicationInfo.FLAG_SYSTEM) == 0 &&
                    (app.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0
        }
>>>>>>> 60f676ca937c5aa0453a2918f1d3009a6b80c889
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
