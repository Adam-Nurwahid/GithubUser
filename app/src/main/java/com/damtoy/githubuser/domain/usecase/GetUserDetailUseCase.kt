package com.damtoy.githubuser.domain.usecase


import com.damtoy.githubuser.domain.Resource
import com.damtoy.githubuser.domain.model.UserDetail
import com.damtoy.githubuser.domain.repository.UserRepository
import javax.inject.Inject

class GetUserDetailUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(username: String): Resource<UserDetail> =
        repository.getUserDetail(username)
}