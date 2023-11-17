package com.example.peekareadapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.peekareadapp.databinding.ScanScreenBinding

/**
 * Screen where the Data of the user should be displayed.
 */
class ScanScreen : Fragment() {

    private var _binding: ScanScreenBinding? = null
    private lateinit var PreferencesViewModel: Preferences

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ScanScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonToTextScreen.setOnClickListener {
            findNavController().navigate(R.id.action_toTextScreen)
        }
    }
}