package com.damtoy.githubuser.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponseDto(
    @Json(name = "total_count") val totalCount: Int = 0,
    @Json(name = "items") val items: List<UserDto> = emptyList()
)

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "id") val id: Long,
    @Json(name = "login") val login: String,
    @Json(name = "avatar_url") val avatarUrl: String
)

@JsonClass(generateAdapter = true)
data class UserDetailDto(
    @Json(name = "id") val id: Long,
    @Json(name = "login") val login: String,
    @Json(name = "avatar_url") val avatarUrl: String,
    @Json(name = "name") val name: String? = null,
    @Json(name = "bio") val bio: String? = null,
    @Json(name = "company") val company: String? = null,
    @Json(name = "location") val location: String? = null,
    @Json(name = "blog") val blog: String? = null,
    @Json(name = "public_repos") val publicRepos: Int? = null,
    @Json(name = "followers") val followers: Int? = null,
    @Json(name = "following") val following: Int? = null,
    @Json(name = "html_url") val htmlUrl: String? = null
)
