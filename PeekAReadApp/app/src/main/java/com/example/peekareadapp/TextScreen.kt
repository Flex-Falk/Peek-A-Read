package com.example.peekareadapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.peekareadapp.databinding.TextScreenBinding

/**
 * Screen where a drink should be selected.
 */
class TextScreen : Fragment() {

    private var _binding: TextScreenBinding? = null
    private lateinit var PreferencesViewModel: Preferences
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TextScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PreferencesViewModel = ViewModelProvider(requireActivity()).get(Preferences::class.java)

    }
}