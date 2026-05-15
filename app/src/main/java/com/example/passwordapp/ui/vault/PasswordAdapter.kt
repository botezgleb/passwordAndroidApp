package com.example.passwordapp.ui.vault

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordapp.data.local.PasswordEntity
import com.example.passwordapp.databinding.ItemPasswordBinding
import com.example.passwordapp.R

class PasswordAdapter(
    private var passwords: List<PasswordEntity>,
    private val onItemClick: (PasswordEntity) -> Unit,
    private val onFavoriteClick: (PasswordEntity) -> Unit
) : RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder>() {

    inner class PasswordViewHolder(
        private val binding: ItemPasswordBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(password: PasswordEntity) {

            binding.titleText.text = password.title

            binding.usernameText.text = password.username

            binding.passwordText.text =
                "•".repeat(password.password.length)

            binding.categoryText.text = password.category

            binding.favoriteButton.setImageResource(
                if (password.isFavorite)
                    R.drawable.ic_star_filled
                else
                    R.drawable.ic_star_outline
            )

            binding.root.setOnClickListener {

                onItemClick(password)
            }

            binding.favoriteButton.setOnClickListener {

                onFavoriteClick(password)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PasswordViewHolder {

        val binding = ItemPasswordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PasswordViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PasswordViewHolder,
        position: Int
    ) {

        holder.bind(passwords[position])
    }

    override fun getItemCount(): Int {

        return passwords.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatePasswords(
        newPasswords: List<PasswordEntity>
    ) {

        passwords = newPasswords

        notifyDataSetChanged()
    }

    fun getPasswordAt(
        position: Int
    ): PasswordEntity {

        return passwords[position]
    }

    fun getItem(position: Int): PasswordEntity {
        return passwords[position]
    }
}