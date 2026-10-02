package com.damtoy.githubuser.domain.usecase


import com.damtoy.githubuser.domain.Resource
import com.damtoy.githubuser.domain.model.User
import com.damtoy.githubuser.domain.repository.UserRepository
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(query: String): Resource<List<User>> {
        val trimmed = query.trim()
        return if (trimmed.isEmpty()) {
            Resource.Success(emptyList())
        } else {
            repository.searchUsers(trimmed)
        }
    }
}