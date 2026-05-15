package com.example.passwordapp.ui.vault

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.passwordapp.R
import com.example.passwordapp.databinding.FragmentVaultBinding
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.passwordapp.data.local.PasswordEntity
import androidx.recyclerview.widget.RecyclerView

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
            }
        )

        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val password = (binding.passwordRecycler.adapter as PasswordAdapter)
                    .getItem(viewHolder.adapterPosition)

                when (direction) {

                    ItemTouchHelper.RIGHT -> {
                        viewModel.toggleFavorite(password)
                    }

                    ItemTouchHelper.LEFT -> {
                        viewModel.deletePassword(password)
                    }
                }
            }
        }

        ItemTouchHelper(swipeCallback)
            .attachToRecyclerView(binding.passwordRecycler)

        binding.passwordRecycler.layoutManager =
            LinearLayoutManager(requireContext())

        binding.passwordRecycler.adapter = adapter

        viewModel.passwords.observe(viewLifecycleOwner) {

            adapter.updatePasswords(it)
        }

        val swipeHandler = object : SwipeCallback() {

            override fun onSwiped(
                viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                direction: Int
            ) {

                val position =
                    viewHolder.adapterPosition

                val password =
                    adapter.getPasswordAt(position)

                when (direction) {

                    ItemTouchHelper.LEFT -> {

                        viewModel.deletePassword(password)
                    }

                    ItemTouchHelper.RIGHT -> {

                        viewModel.toggleFavorite(password)

                        adapter.notifyItemChanged(position)
                    }
                }
            }
        }

        val itemTouchHelper =
            ItemTouchHelper(swipeHandler)

        itemTouchHelper.attachToRecyclerView(
            binding.passwordRecycler
        )

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

fun PasswordViewModel.deletePassword(password: PasswordEntity) {}
