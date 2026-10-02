package com.damtoy.githubuser.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Long,
    val login: String,
    val avatarUrl: String,
    val name: String? = null,
    val bio: String? = null,
    val company: String? = null,
    val location: String? = null,
    val blog: String? = null,
    val publicRepos: Int? = null,
    val followers: Int? = null,
    val following: Int? = null,
    val htmlUrl: String? = null,
    val isDetailCached: Boolean = false,
    val isFavorite: Boolean = false
)