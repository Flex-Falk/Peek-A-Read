package com.example.peekareadapp

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.PermissionState
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.foundation.Image
import coil.compose.rememberImagePainter
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.TextField
import androidx.compose.foundation.isSystemInDarkTheme


/**
 * enum values that represent the screens in the app
 */
enum class PeekAReadScreen(@StringRes val title: Int) {
    Camera(title = R.string.CameraScreen),
    Scan(title = R.string.ScanScreen),
    Text(title = R.string.TextScreen),
    Preferences(title = R.string.PreferencesScreen),
    Start(title = R.string.app_name)
}

var alreadyAskedForPreferences: Boolean = false
lateinit var imageUri: Uri

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeekAReadAppBar(
    currentScreen: PeekAReadScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    navigateToPreferences: () -> Unit,
    isPreferencesButtonEnabled: Boolean,
    modifier: Modifier = Modifier,
    ) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.BackButtton)
                    )
                }
            }
        },
        actions = {
            // Add the settings icon to the top app bar only if it's enabled
            if (isPreferencesButtonEnabled) {
                IconButton(onClick = navigateToPreferences) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(R.string.action_settings)
                    )
                }
            }
        }
    )
}
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun handleMissingCameraPermission(context: Context, cameraPermissionState: PermissionState) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (alreadyAskedForPreferences == false) {
            Text("Die Kamera ist wichtig für diese App. Bitte erlauben Sie Peek-A-Read den Zugriff darauf.")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { cameraPermissionState.launchPermissionRequest(); alreadyAskedForPreferences = true
            }) {
                Text("Zugriff erlauben")
            }
        } else {
            Text("Die Kamera ist nicht verfügbar.")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:" + context.packageName)
                context.startActivity(intent)
            }) {
                Text("Zugriff erlauben")
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PeekAReadApp(
    navController: NavHostController = rememberNavController(),
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = PeekAReadScreen.valueOf(
        backStackEntry?.destination?.route ?: PeekAReadScreen.Start.name
    )

    val context = LocalContext.current

    val sharedPreferences = remember {
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    }

    var preferences_fontType by remember { mutableStateOf("Arial") }
    var preferences_darkmode by remember { mutableStateOf(false) }
    val options = listOf("Arial", "Helvetica", "Unit")

    // Load font type and dark mode boolean from SharedPreferences
    fun loadPreferences() {
        preferences_fontType = sharedPreferences.getString("fontType", options[0]) ?: options[0]
        preferences_darkmode = sharedPreferences.getBoolean("darkMode", false)
    }

    // Save font type and dark mode boolean to SharedPreferences
    fun savePreferences() {
        with(sharedPreferences.edit()) {
            putString("fontType", preferences_fontType)
            putBoolean("darkMode", preferences_darkmode)
            apply()
        }
    }

    DisposableEffect(Unit) {
        loadPreferences()
        onDispose {
            savePreferences()
        }
    }

    MaterialTheme(
        colorScheme = if (preferences_darkmode || isSystemInDarkTheme()) DarkColors else LightColors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
    ) {
        Scaffold(
            topBar = {
                PeekAReadAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() },
                    navigateToPreferences = { navController.navigate(PeekAReadScreen.Preferences.name)},
                    isPreferencesButtonEnabled = currentScreen != PeekAReadScreen.Preferences
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = PeekAReadScreen.Camera.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = PeekAReadScreen.Camera.name) {
                    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
                    val context = LocalContext.current
                    val lifecycleOwner = LocalLifecycleOwner.current

                    if (cameraPermissionState.status.isGranted) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            //Text(text = "Hier wird die App in der Kameraansicht gestartet.")
                            CameraPreview(context = context, lifecycleOwner = lifecycleOwner, navController = navController)

//                        FloatingActionButton(onClick = { navController.navigate(PeekAReadScreen.Scan.name) }) {
//                            Icon(Icons.Filled.Add, "Floating action button.")
//                        }
                        }
                    } else {
                        handleMissingCameraPermission(LocalContext.current, cameraPermissionState)
                    }
                }
                composable(route = PeekAReadScreen.Scan.name) {
                    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                    val context = LocalContext.current

                    val image: InputImage
                    try {
                        image = InputImage.fromFilePath(context, imageUri)
                        val result = recognizer.process(image)
                            .addOnSuccessListener { visionText ->
                                // Task completed successfully
                                val resultText = visionText.text
                                Log.i("Scan", resultText)
                            }
                            .addOnFailureListener { e ->
                                // Task failed with an exception
                                val msg = "Capture failed: ${e.message}"
                                Log.i("Scan", msg)
                            }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }


                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
//                    Text(text = "Hier werden Elemente aus dem geschossenen Bild ausgewählt.")
                        Image(
                            modifier = Modifier
                                .padding(16.dp, 8.dp),
                            painter = rememberImagePainter(imageUri),
                            contentDescription = null
                        )
                        FloatingActionButton(onClick =  { navController.navigate(PeekAReadScreen.Text.name) }){
                            Icon(Icons.Filled.Add, "Floating action button.")
                        }
                    }
                }
                composable(route = PeekAReadScreen.Text.name) {
                    var sliderPosition by remember { mutableStateOf(0f) } // Initialize with the default value (aka 0)

                    //text-to-speech context
                    val context = LocalContext.current
                    var textToSpeech: TextToSpeech? by remember{ mutableStateOf(null) }
                    // text to read aloud
                    var readText = stringResource(R.string.LoremIpsum)

                    DisposableEffect(Unit){
                        textToSpeech = TextToSpeech(context){ status ->
                            if(status == TextToSpeech.SUCCESS) {
                                textToSpeech?.language = Locale.GERMAN
                            }
                        }

                        onDispose {
                            textToSpeech?.stop()
                            textToSpeech?.shutdown()
                        }
                    }
                    Scaffold(
                        bottomBar = {
                            BottomAppBar(
                                actions = {
                                    IconButton(onClick = {sliderPosition -= 0.1f}) {
                                        Icon(painterResource(id = R.drawable.baseline_text_decrease_24), "Localized description")
                                    }
                                    Slider(
                                        value = sliderPosition,
                                        onValueChange = { newValue -> sliderPosition = newValue },
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .height(40.dp)
                                            .width(150.dp)
                                    )

                                    IconButton(onClick = {sliderPosition += 0.1f}) {
                                        Icon(painterResource(id = R.drawable.baseline_text_increase_24), "Localized description")
                                    }
                                },
                                floatingActionButton = {
                                    FloatingActionButton(
                                        onClick = {
                                            //text-to-speech
                                            textToSpeech?.speak(
                                                readText,
                                                TextToSpeech.QUEUE_FLUSH,
                                                null,
                                                null
                                            )
                                        },
                                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                                    ) {
                                        Icon(painterResource(id = R.drawable.baseline_volume_up_24), "Localized description")
                                    }
                                }
                            )
                        },
                    ) { innerPadding ->

                        val fontSize = (16 * sliderPosition + 20).sp // Adjust the base size (16) based on the slider position
                        val lineHeight = fontSize * 1.25

                        Text(
                            modifier = Modifier.padding(innerPadding),
                            text = stringResource(R.string.LoremIpsum),
                            fontSize = fontSize,
                            lineHeight = lineHeight
                        )
                    }
                }
                composable(route = PeekAReadScreen.Preferences.name) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text("Dunkelmodus aktiviert")

                        Switch(
                            checked = preferences_darkmode,
                            onCheckedChange = {
                                preferences_darkmode = it
                                // Save the updated dark mode preference
                                savePreferences()
                            }
                        )


                        Spacer(modifier = Modifier.height(40.dp))

                        var expanded by remember { mutableStateOf(false) }

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded },
                        ) {
                            TextField(
                                // The `menuAnchor` modifier must be passed to the text field for correctness.
                                modifier = Modifier.menuAnchor(),
                                readOnly = true,
                                value = preferences_fontType,
                                onValueChange = {},
                                label = { Text("Schriftart") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                            ) {
                                options.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption) },
                                        onClick = {
                                            preferences_fontType = selectionOption
                                            expanded = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun CameraPreview(context: Context, lifecycleOwner: LifecycleOwner, navController: NavHostController){
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var preview by remember { mutableStateOf<Preview?>(null) }
    val executor = ContextCompat.getMainExecutor(context)
    val cameraProvider = cameraProviderFuture.get()
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }

        Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ){
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                cameraProviderFuture.addListener(
                    { val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageCapture
                        )
                    }, executor)
                preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                previewView
            }
        )
        Column (
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Bottom
        ){
            IconButton(
                modifier = Modifier.padding(bottom = 20.dp),
                onClick = { takePhoto(imageCapture, context, navController) }
            ) {
                Icon(Icons.Filled.Add, "Take photo", tint = Color.White,
                    modifier = Modifier
                        .size(80.dp)
                        .border(1.dp, Color.White, CircleShape))
            }
        }
    }
}

fun takePhoto(imageCapture: ImageCapture, context: Context, navController: NavHostController) {

    val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
        .format(System.currentTimeMillis())
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }
    }

    // Create output options object which contains file + metadata
    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues)
        .build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                val msg = "Capture failed: ${exc.message}"
                Log.i("Capture",msg)
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val msg = "Captured: ${output.savedUri}"
                imageUri = output.savedUri!!
                Log.i("Capture",msg)
                navController.navigate(PeekAReadScreen.Scan.name)

            }
        }
    )
}

