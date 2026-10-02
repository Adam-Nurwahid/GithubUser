package com.damtoy.githubuser.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert

@Dao
abstract class UserDao {

    @Query("SELECT * FROM users WHERE login LIKE '%' || :query || '%' ORDER BY login COLLATE NOCASE LIMIT 30")
    abstract suspend fun searchByLogin(query: String): List<UserEntity>

    @Query("SELECT * FROM users WHERE login = :login COLLATE NOCASE LIMIT 1")
    abstract suspend fun getByLogin(login: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertIgnore(users: List<UserEntity>): List<Long>

    @Query("UPDATE users SET login = :login, avatarUrl = :avatarUrl WHERE id = :id")
    abstract suspend fun updateBasic(id: Long, login: String, avatarUrl: String)

    @Upsert
    abstract suspend fun upsert(user: UserEntity)

    /**
     * Saves search results without wiping detail fields that were cached earlier:
     * insert new rows, and for existing rows only refresh login + avatar.
     */
    @Transaction
    open suspend fun upsertBasic(users: List<UserEntity>) {
        val rowIds = insertIgnore(users)
        users.forEachIndexed { index, user ->
            if (rowIds[index] == -1L) updateBasic(user.id, user.login, user.avatarUrl)
        }
    }
}