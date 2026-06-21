package com.example.passwordapp.ui.details

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.passwordapp.R
import com.example.passwordapp.databinding.FragmentPasswordDetailsBinding
import com.example.passwordapp.ui.vault.PasswordViewModel
import com.example.passwordapp.ui.vault.PasswordViewModelFactory

class PasswordDetailsFragment :
    Fragment(R.layout.fragment_password_details) {

    private var _binding:
            FragmentPasswordDetailsBinding? = null

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

        super.onViewCreated(view, savedInstanceState)

        _binding =
            FragmentPasswordDetailsBinding.bind(view)

        val passwordId =
            requireArguments().getInt("passwordId")

        viewModel.passwords.observe(
            viewLifecycleOwner
        ) { passwords ->

            val password =
                passwords.find { it.id == passwordId }

            password?.let { passwordEntity ->

                binding.titleText.text = passwordEntity.title
                binding.usernameText.text = passwordEntity.username
                binding.passwordText.text = passwordEntity.password
                binding.categoryText.text = passwordEntity.category

                binding.copyButton.setOnClickListener {

                    val clipboard =
                        requireContext().getSystemService(
                            Context.CLIPBOARD_SERVICE
                        ) as ClipboardManager

                    val clip = ClipData.newPlainText(
                        "password",
                        passwordEntity.password
                    )

                    clipboard.setPrimaryClip(clip)

                    Toast.makeText(
                        requireContext(),
                        "Password copied",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                binding.editButton.setOnClickListener {
                    val bundle = Bundle().apply {
                        putInt("passwordId", passwordId)
                    }
                    findNavController().navigate(
                        R.id.addPasswordFragment,
                        bundle
                    )
                }
            }
        }

        binding.toolbar.setNavigationOnClickListener {

            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}