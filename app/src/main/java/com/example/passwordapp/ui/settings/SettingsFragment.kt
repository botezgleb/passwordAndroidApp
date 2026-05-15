package com.example.passwordapp.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.passwordapp.R
import com.example.passwordapp.databinding.FragmentSettingsBinding
import android.content.Intent
import com.example.passwordapp.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment :
    Fragment(R.layout.fragment_settings) {

    private var _binding:
            FragmentSettingsBinding? = null

    private val binding get() = _binding!!

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

        binding.lockSwitch.setOnCheckedChangeListener {
                _, isChecked ->

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

                        AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_NO
                        )
                    }

                    R.id.darkTheme -> {

                        AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_YES
                        )
                    }
                }
            }

        binding.logoutButton.setOnClickListener {

            FirebaseAuth
                .getInstance()
                .signOut()

            startActivity(
                Intent(
                    requireContext(),
                    LoginActivity::class.java
                )
            )

            requireActivity().finish()
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}