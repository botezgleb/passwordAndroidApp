package com.example.passwordapp.ui.security

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.passwordapp.R
import com.example.passwordapp.databinding.FragmentSecurityBinding
import com.example.passwordapp.ui.vault.PasswordViewModel
import com.example.passwordapp.ui.vault.PasswordViewModelFactory

class SecurityFragment :
    Fragment(R.layout.fragment_security) {

    private var _binding:
            FragmentSecurityBinding? = null

    private val binding get() = _binding!!

    private val viewModel:
            PasswordViewModel by activityViewModels {

        PasswordViewModelFactory(
            requireActivity().application
        )
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
            FragmentSecurityBinding.bind(view)

        viewModel.passwords.observe(
            viewLifecycleOwner
        ) { passwords ->

            val total =
                passwords.size

            val weak =
                passwords.count {

                    getPasswordStrength(
                        it.password
                    ) == "Weak"
                }

            val strong =
                passwords.count {

                    val strength =
                        getPasswordStrength(
                            it.password
                        )

                    strength == "Strong" ||
                            strength == "Very Strong"
                }

            val reused =
                passwords.groupBy {
                    it.password
                }.count {
                    it.value.size > 1
                }

            binding.totalText.text =
                total.toString()

            binding.weakText.text =
                weak.toString()

            binding.strongText.text =
                strong.toString()

            binding.reusedText.text =
                reused.toString()
        }
    }

    private fun getPasswordStrength(
        password: String
    ): String {

        var score = 0

        if (password.length >= 8) score++

        if (
            password.any {
                it.isUpperCase()
            }
        ) score++

        if (
            password.any {
                it.isDigit()
            }
        ) score++

        if (
            password.any {
                !it.isLetterOrDigit()
            }
        ) score++

        return when (score) {

            0, 1 -> "Weak"

            2 -> "Medium"

            3 -> "Strong"

            else -> "Very Strong"
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}