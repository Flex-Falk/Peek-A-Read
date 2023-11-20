package com.example.peekareadapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            PeekAReadTheme {
                PeekAReadApp()
            }
        }
    }
}

@Composable
fun PeekAReadTheme(content: @Composable (PaddingValues) -> Unit) {
    MaterialTheme {
        Surface {
            content(PaddingValues(0.dp))
        }
    }
}

   /* // Permissions stuff
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
    ) {
        // Permission is not granted
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
    }


// Called automatically when checking permissions.
 fun onRequestPermissionsResult(
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
}*/

