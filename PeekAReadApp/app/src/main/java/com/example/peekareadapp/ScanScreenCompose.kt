package com.example.peekareadapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Icon


@Composable
fun ScanScreenCompose (
    state: ScanScreenState,
    toTextScreen: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Hier werden Elemente aus dem geschossenen Bild ausgewählt.")
        FloatingActionButton(onClick = toTextScreen){
            Icon(Icons.Filled.Add, "Floating action button.")
        }
    }
}
