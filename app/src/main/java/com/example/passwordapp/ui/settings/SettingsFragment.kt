package com.example.passwordapp.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.passwordapp.R
import com.example.passwordapp.databinding.FragmentSettingsBinding
import com.example.passwordapp.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment :
    Fragment(R.layout.fragment_settings) {

    private var _binding:
            FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    private val sharedPrefs by lazy {
        requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(
            view,
            savedInstanceState
        )

        _binding =
            FragmentSettingsBinding.bind(view)

        // Load saved settings
        binding.lockSwitch.isChecked = sharedPrefs.getBoolean("app_lock", false)
        binding.clipboardSwitch.isChecked = sharedPrefs.getBoolean("clear_clipboard", true)
        
        val isDarkMode = sharedPrefs.getBoolean("dark_mode", false)
        if (isDarkMode) {
            binding.darkTheme.isChecked = true
        } else {
            binding.lightTheme.isChecked = true
        }

        binding.lockSwitch.setOnCheckedChangeListener {
                _, isChecked ->

            sharedPrefs.edit().putBoolean("app_lock", isChecked).apply()
            
            Toast.makeText(
                requireContext(),
                if (isChecked)
                    "App Lock Enabled"
                else
                    "App Lock Disabled",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.clipboardSwitch
            .setOnCheckedChangeListener {
                    _, isChecked ->

                sharedPrefs.edit().putBoolean("clear_clipboard", isChecked).apply()

                Toast.makeText(
                    requireContext(),
                    if (isChecked)
                        "Clipboard Auto-Clear Enabled"
                    else
                        "Clipboard Auto-Clear Disabled",
                    Toast.LENGTH_SHORT
                ).show()
            }

        binding.themeGroup
            .setOnCheckedChangeListener {
                    _, checkedId ->

                when (checkedId) {

                    R.id.lightTheme -> {
                        sharedPrefs.edit().putBoolean("dark_mode", false).apply()
                        AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_NO
                        )
                    }

                    R.id.darkTheme -> {
                        sharedPrefs.edit().putBoolean("dark_mode", true).apply()
                        AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_YES
                        )
                    }
                }
            }

        binding.logoutButton.setOnClickListener {

            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout") { _, _ ->
                    FirebaseAuth
                        .getInstance()
                        .signOut()

                    val intent = Intent(
                        requireContext(),
                        LoginActivity::class.java
                    )
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}