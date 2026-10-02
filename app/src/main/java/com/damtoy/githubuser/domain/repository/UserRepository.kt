package com.damtoy.githubuser.domain.repository

import com.damtoy.githubuser.domain.Resource
import com.damtoy.githubuser.domain.model.User
import com.damtoy.githubuser.domain.model.UserDetail


interface UserRepository {
    suspend fun searchUsers(query: String): Resource<List<User>>
    suspend fun getUserDetail(username: String): Resource<UserDetail>
}
