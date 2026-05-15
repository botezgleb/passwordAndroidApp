package com.example.passwordapp.ui.vault

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.passwordapp.data.local.PasswordDatabase
import com.example.passwordapp.data.local.PasswordEntity
import com.example.passwordapp.data.repository.PasswordRepository
import kotlinx.coroutines.launch

class PasswordViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dao =
        PasswordDatabase
            .getDatabase(application)
            .passwordDao()

    private val repository =
        PasswordRepository(dao)

    val passwords:
            LiveData<List<PasswordEntity>> =
        repository.allPasswords

    val favoritePasswords:
            LiveData<List<PasswordEntity>> =
        dao.getFavoritePasswords()

    fun addPassword(
        password: PasswordEntity
    ) {

        viewModelScope.launch {

            repository.insertPassword(password)
        }
    }

    fun toggleFavorite(
        password: PasswordEntity
    ) {

        viewModelScope.launch {

            repository.updateFavoriteStatus(
                password.id,
                !password.isFavorite
            )
        }
    }

    fun searchPasswords(
        query: String
    ): LiveData<List<PasswordEntity>> {

        return repository.searchPasswords(query)
    }

    fun deletePassword(password: PasswordEntity) {
        viewModelScope.launch {
            repository.deletePassword(password)
        }
    }
}