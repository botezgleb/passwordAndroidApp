package com.example.passwordapp.ui.vault

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.passwordapp.R
import com.example.passwordapp.databinding.FragmentVaultBinding

class VaultFragment : Fragment(R.layout.fragment_vault) {

    private var _binding: FragmentVaultBinding? = null

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

        _binding = FragmentVaultBinding.bind(view)

        val adapter = PasswordAdapter(

            passwords = emptyList(),

            onItemClick = { password ->

                val bundle = Bundle()

                bundle.putInt(
                    "passwordId",
                    password.id
                )

                findNavController().navigate(
                    R.id.action_vaultFragment_to_passwordDetailsFragment,
                    bundle
                )
            },

            onFavoriteClick = { password ->

                viewModel.toggleFavorite(password)
            },

            onDeleteClick = { password ->

                androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Delete password")
                    .setMessage(
                        "Are you sure you want to delete this password?"
                    )
                    .setPositiveButton("Delete") { _, _ ->

                        viewModel.deletePassword(password)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            },

            onEditClick = { password ->

            }
        )

        binding.passwordRecycler.layoutManager =
            LinearLayoutManager(requireContext())

        binding.passwordRecycler.adapter = adapter

        viewModel.passwords.observe(viewLifecycleOwner) {

            adapter.updatePasswords(it)
        }

        binding.addButton.setOnClickListener {

            findNavController().navigate(
                R.id.addPasswordFragment
            )
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}