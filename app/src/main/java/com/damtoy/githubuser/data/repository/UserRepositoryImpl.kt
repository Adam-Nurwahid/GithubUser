package com.damtoy.githubuser.data.repository

import com.damtoy.githubuser.data.local.UserDao
import com.damtoy.githubuser.data.mapper.toDetail
import com.damtoy.githubuser.data.mapper.toDomain
import com.damtoy.githubuser.data.mapper.toEntity
import com.damtoy.githubuser.data.remote.GithubApi
import com.damtoy.githubuser.domain.Resource
import com.damtoy.githubuser.domain.model.User
import com.damtoy.githubuser.domain.model.UserDetail
import com.damtoy.githubuser.domain.repository.UserRepository
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.CancellationException
import javax.inject.Inject

/**
 * Network-first strategy: always try the API, persist the result in Room,
 * and fall back to the cached copy if the request fails.
 */
class UserRepositoryImpl @Inject constructor(
    private val api: GithubApi,
    private val dao: UserDao
) : UserRepository {

    override suspend fun searchUsers(query: String): Resource<List<User>> =
        try {
            val entities = api.searchUsers(query, PAGE_SIZE).items.map { it.toEntity() }
            dao.upsertBasic(entities)
            Resource.Success(entities.map { it.toDomain() })
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val cached = dao.searchByLogin(query)
            if (cached.isNotEmpty()) {
                Resource.Success(cached.map { it.toDomain() }, isFromCache = true)
            } else {
                Resource.Error(e.toUserMessage())
            }
        }

    override suspend fun getUserDetail(username: String): Resource<UserDetail> =
        try {
            val entity = api.getUserDetail(username).toEntity()
            dao.upsert(entity)
            Resource.Success(entity.toDetail())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val cached = dao.getByLogin(username)
            if (cached != null && cached.isDetailCached) {
                Resource.Success(cached.toDetail(), isFromCache = true)
            } else {
                Resource.Error(e.toUserMessage())
            }
        }

    private fun Throwable.toUserMessage(): String = when (this) {
        is IOException -> "No internet connection. Please check your network."
        is HttpException -> when (code()) {
            403, 429 -> "GitHub API rate limit reached. Please try again later."
            404 -> "User not found."
            422 -> "Invalid search query."
            else -> "Server error (${code()}). Please try again."
        }
        else -> "Something went wrong. Please try again."
    }

    private companion object {
        const val PAGE_SIZE = 30
    }
}