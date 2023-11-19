package com.example.peekareadapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

/**
 * Screen where the Data of the user should be displayed.
 */
class ScanScreen : Fragment() {
    private lateinit var composeView: ComposeView
    private val viewModel: ScanScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        composeView.setContent {
            val state by viewModel.state.collectAsState()
            ScanScreenCompose(
                state = state,
                toTextScreen = {
                    findNavController().navigate(R.id.action_toTextScreen)
                })
        }
    }
}