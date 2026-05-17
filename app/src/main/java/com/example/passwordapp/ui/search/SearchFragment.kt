package com.example.passwordapp.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.passwordapp.R
import com.example.passwordapp.databinding.FragmentSearchBinding
import com.example.passwordapp.ui.vault.PasswordAdapter
import com.example.passwordapp.ui.vault.PasswordViewModel
import com.example.passwordapp.ui.vault.PasswordViewModelFactory
import androidx.core.widget.addTextChangedListener

class SearchFragment :
    Fragment(R.layout.fragment_search) {

    private var _binding:
            FragmentSearchBinding? = null

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
            FragmentSearchBinding.bind(view)

        binding.searchRecycler.layoutManager =
            LinearLayoutManager(requireContext())

        binding.searchEdit.addTextChangedListener {

            val query = it.toString()

            viewModel.searchPasswords(query)
                .observe(viewLifecycleOwner) { passwords ->

                    binding.searchRecycler.adapter =
                        PasswordAdapter(

                            passwords = passwords,

                            onItemClick = {},

                            onFavoriteClick = { password ->

                                viewModel.toggleFavorite(password)
                            },

                            onDeleteClick = { password ->

                                viewModel.deletePassword(password)
                            },

                            onEditClick = { password ->

                            }
                        )
                }
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}