package com.example.passwordapp.data.repository

import androidx.lifecycle.LiveData
import com.example.passwordapp.data.local.PasswordDao
import com.example.passwordapp.data.local.PasswordEntity

class PasswordRepository(
    private val dao: PasswordDao
) {

    val allPasswords:
            LiveData<List<PasswordEntity>> =
        dao.getAllPasswords()

    suspend fun insertPassword(
        password: PasswordEntity
    ) {
        dao.insertPassword(password)
    }

    suspend fun updatePassword(
        password: PasswordEntity
    ) {
        dao.updatePassword(password)
    }

    suspend fun updateFavoriteStatus(
        id: Int,
        isFavorite: Boolean
    ) {

        dao.updateFavoriteStatus(
            id,
            isFavorite
        )
    }

    suspend fun deletePassword(password: PasswordEntity) {
        dao.deletePassword(password)
    }

    fun searchPasswords(
        query: String
    ): LiveData<List<PasswordEntity>> {

        return dao.searchPasswords(query)
    }
}