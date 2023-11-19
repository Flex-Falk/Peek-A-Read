package com.example.peekareadapp

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextScreenCompose (
    state: TextScreenState,
){
    var sliderPosition by remember { mutableStateOf(0f) } // Initialize with the default value (aka 0)

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
                        onClick = { /* read the text */ },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(painterResource(id = R.drawable.baseline_volume_up_24), "Localized description")
                    }
                }
            )
        },
    ) { innerPadding ->
        Text(
            modifier = Modifier.padding(innerPadding),
            text = "lrnglwrjngjlnwrgrnwglwngk\n" +
                    "kwjrgkjwbrg kwjbfgkwjrbf kwbjdfkjwrbf kjbfkjbwrf kjbfkwgjrbgkjrwg kjbdfkjbwrfjk kjbfgkwrjb",
            fontSize = (16 * sliderPosition + 20).sp // Adjust the base size (16) based on the slider position
        )
    }
}
