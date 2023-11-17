package com.example.peekareadapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.peekareadapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import android.provider.Settings

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Permissions stuff
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }

        // Navigation stuff
        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Set up destination change listener
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Check if the new destination is CameraScreen.
            if (destination.id == R.id.CameraScreen) {
                // Hide the back button when in CameraScreen.
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            } else {
                // Show the back button for other destinations.
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // Replace with the actual navigation action for the desired screen.
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_toPreferencesScreen)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Check if the current destination is CameraScreen.
        if (navController.currentDestination?.id == R.id.CameraScreen) {
            // Nullify the back action when in CameraScreen.
        } else {
            super.onBackPressed()
        }
    }

    // Called automatically when checking permissions.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now use the camera.
            } else {
                // Permission denied, show a message to the user.
                showPermissionDeniedMessage()
            }
    }

    // Shows a message, that leads to the app settings, if permissions are not granted yet.
    private fun showPermissionDeniedMessage() {
        Snackbar.make(
            binding.root,
            "FEHLER: Kamerazugriff ist nötig zum Verwenden der App.",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Einstellungen") {
            // Open app settings to allow the user to grant permission.
            openAppSettings()
        }.show()
    }

    // Opens the app settings to allow the user to grant permissions.
    private fun openAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + this.packageName)
        )
        startActivity(intent)
    }

}

