package com.damtoy.githubuser.domain.usecase

import com.damtoy.githubuser.domain.Resource
import com.damtoy.githubuser.domain.model.User
import com.damtoy.githubuser.domain.repository.UserRepository
import javax.inject.Inject

class FavoriteUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend fun getFavorites(): Resource<List<User>> =
        repository.getFavorites()

    suspend fun setFavorite(
        id: Long,
        favorite: Boolean
    ): Resource<Unit> =
        repository.setFavorite(id, favorite)
}