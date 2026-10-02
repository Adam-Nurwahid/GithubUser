package com.damtoy.githubuser.data.remote

import com.damtoy.githubuser.data.remote.dto.SearchResponseDto
import com.damtoy.githubuser.data.remote.dto.UserDetailDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("per_page") perPage: Int
    ): SearchResponseDto

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String
    ): UserDetailDto
}
