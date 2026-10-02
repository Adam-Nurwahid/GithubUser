package com.damtoy.githubuser.domain.model


data class UserDetail(
    val id: Long,
    val login: String,
    val avatarUrl: String,
    val name: String?,
    val bio: String?,
    val company: String?,
    val location: String?,
    val blog: String?,
    val publicRepos: Int,
    val followers: Int,
    val following: Int,
    val htmlUrl: String,
    val isFavorite: Boolean = false
)
