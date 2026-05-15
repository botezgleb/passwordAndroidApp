package com.example.passwordapp.ui.add

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.passwordapp.R
import com.example.passwordapp.data.local.PasswordEntity
import com.example.passwordapp.databinding.FragmentAddPasswordBinding
import com.example.passwordapp.ui.vault.PasswordViewModel
import com.example.passwordapp.ui.vault.PasswordViewModelFactory

class AddPasswordFragment : Fragment(R.layout.fragment_add_password) {

    private var _binding: FragmentAddPasswordBinding? = null

    private val binding get() = _binding!!

    private val viewModel: PasswordViewModel by activityViewModels {

        PasswordViewModelFactory(
            requireActivity().application
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAddPasswordBinding.bind(view)

        binding.generateButton.setOnClickListener {

            val generated = generatePassword(16)

            binding.passwordEdit.setText(generated)
        }

        binding.passwordEdit.addTextChangedListener(

            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    val password = s.toString()

                    val strength =
                        getPasswordStrength(password)

                    binding.strengthText.text =
                        "Strength: $strength"
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {}
            }
        )

        binding.toolbar.setNavigationOnClickListener {

            findNavController().popBackStack()
        }

        binding.saveButton.setOnClickListener {

            val title =
                binding.titleEdit.text.toString().trim()

            val username =
                binding.usernameEdit.text.toString().trim()

            val passwordText =
                binding.passwordEdit.text.toString().trim()

            val category =
                binding.categoryEdit.text.toString().trim()

            if (
                title.isEmpty() ||
                username.isEmpty() ||
                passwordText.isEmpty() ||
                category.isEmpty()
            ) {

                if (title.isEmpty()) {
                    binding.titleEdit.error = "Required"
                }

                if (username.isEmpty()) {
                    binding.usernameEdit.error = "Required"
                }

                if (passwordText.isEmpty()) {
                    binding.passwordEdit.error = "Required"
                }

                if (category.isEmpty()) {
                    binding.categoryEdit.error = "Required"
                }

                return@setOnClickListener
            }

            val isFavorite =
                binding.favoriteCheck.isChecked

            val password = PasswordEntity(
                title = title,
                username = username,
                password = passwordText,
                category = category,
                isFavorite = isFavorite
            )

            viewModel.addPassword(password)

            findNavController().popBackStack()
        }
    }

    private fun generatePassword(
        length: Int
    ): String {

        val chars =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "0123456789" +
                    "!@#\$%^&*"

        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }

    private fun getPasswordStrength(
        password: String
    ): String {

        var score = 0

        if (password.length >= 8) score++

        if (password.any { it.isUpperCase() }) score++

        if (password.any { it.isDigit() }) score++

        if (password.any { !it.isLetterOrDigit() }) score++

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