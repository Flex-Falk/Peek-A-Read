package com.example.peekareadapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
class ScanScreenViewModel: ViewModel() {

    private val _state = MutableStateFlow(ScanScreenState())
    val state = _state.asStateFlow()
}

class ScanScreenState