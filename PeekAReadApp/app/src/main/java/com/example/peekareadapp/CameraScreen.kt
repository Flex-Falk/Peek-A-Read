package com.example.peekareadapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.peekareadapp.databinding.CameraScreenBinding

/**
 * Screen where the user data should be put in.
 */
class CameraScreen : Fragment() {

    private var _binding: CameraScreenBinding? = null
    private lateinit var PreferencesViewModel: Preferences

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CameraScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PreferencesViewModel = ViewModelProvider(requireActivity()).get(Preferences::class.java)
        binding.buttonToScanScreen.setOnClickListener {
            findNavController().navigate(R.id.action_toScanScreen)
        }
    }
}