package com.example.passwordapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PasswordDao {

    @Insert
    suspend fun insertPassword(
        password: PasswordEntity
    )

    @Query("SELECT * FROM passwords")
    fun getAllPasswords():
            LiveData<List<PasswordEntity>>

    @Query("SELECT * FROM passwords WHERE isFavorite = 1")
    fun getFavoritePasswords():
            LiveData<List<PasswordEntity>>

    @Query("""
        UPDATE passwords
        SET isFavorite = :isFavorite
        WHERE id = :id
    """)
    suspend fun updateFavoriteStatus(
        id: Int,
        isFavorite: Boolean
    )

    @Query("""
        SELECT * FROM passwords
        WHERE title LIKE '%' || :query || '%'
        OR username LIKE '%' || :query || '%'
        OR category LIKE '%' || :query || '%'
    """)
    fun searchPasswords(
        query: String
    ): LiveData<List<PasswordEntity>>

    @Delete
    suspend fun deletePassword(password: PasswordEntity)
}