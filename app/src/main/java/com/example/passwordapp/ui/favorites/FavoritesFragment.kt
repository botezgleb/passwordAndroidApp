package com.example.passwordapp.ui.favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.passwordapp.R
import com.example.passwordapp.databinding.FragmentFavoritesBinding
import com.example.passwordapp.ui.vault.PasswordAdapter
import com.example.passwordapp.ui.vault.PasswordViewModel
import com.example.passwordapp.ui.vault.PasswordViewModelFactory

class FavoritesFragment :
    Fragment(R.layout.fragment_favorites) {

    private var _binding:
            FragmentFavoritesBinding? = null

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
            FragmentFavoritesBinding.bind(view)

        binding.favoritesRecycler.layoutManager =
            LinearLayoutManager(requireContext())

        viewModel.favoritePasswords.observe(
            viewLifecycleOwner
        ) {

            binding.favoritesRecycler.adapter =
                PasswordAdapter(

                    passwords = it,

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

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}