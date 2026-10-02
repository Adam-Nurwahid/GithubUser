package com.damtoy.githubuser.data.mapper

import com.damtoy.githubuser.data.local.UserEntity
import com.damtoy.githubuser.data.remote.dto.UserDetailDto
import com.damtoy.githubuser.data.remote.dto.UserDto
import com.damtoy.githubuser.domain.model.User
import com.damtoy.githubuser.domain.model.UserDetail

fun UserDto.toEntity() = UserEntity(
    id = id,
    login = login,
    avatarUrl = avatarUrl
)

fun UserDetailDto.toEntity(
    isFavorite: Boolean = false
) = UserEntity(
    id = id,
    login = login,
    avatarUrl = avatarUrl,
    name = name,
    bio = bio,
    company = company,
    location = location,
    blog = blog,
    publicRepos = publicRepos ?: 0,
    followers = followers ?: 0,
    following = following ?: 0,
    htmlUrl = htmlUrl ?: "https://github.com/$login",
    isDetailCached = true,
    isFavorite = isFavorite
)

fun UserEntity.toDomain() = User(
    id = id,
    login = login,
    avatarUrl = avatarUrl,
    isFavorite = isFavorite
)

fun UserEntity.toDetail() = UserDetail(
    id = id,
    login = login,
    avatarUrl = avatarUrl,
    name = name,
    bio = bio,
    company = company,
    location = location,
    blog = blog,
    publicRepos = publicRepos ?: 0,
    followers = followers ?: 0,
    following = following ?: 0,
    htmlUrl = htmlUrl ?: "https://github.com/$login",
    isFavorite = isFavorite
)