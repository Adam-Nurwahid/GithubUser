package com.damtoy.githubuser.data

import com.damtoy.githubuser.data.local.UserDao
import com.damtoy.githubuser.data.local.UserEntity
import com.damtoy.githubuser.data.remote.dto.UserDetailDto
import com.damtoy.githubuser.data.remote.GithubApi
import com.damtoy.githubuser.data.remote.dto.SearchResponseDto
import com.damtoy.githubuser.data.remote.dto.UserDto
import com.damtoy.githubuser.data.repository.UserRepositoryImpl
import com.damtoy.githubuser.domain.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class UserRepositoryImplTest {

    private val api = mockk<GithubApi>()
    private val dao = mockk<UserDao>()
    private val repository = UserRepositoryImpl(api, dao)

    @Test
    fun `searchUsers saves remote result to database and returns it`() = runTest {
        coEvery { api.searchUsers("adam", 30) } returns
                SearchResponseDto(1, listOf(UserDto(1, "adam", "avatar")))
        coEvery { dao.upsertBasic(any()) } just runs

        val result = repository.searchUsers("adam") as Resource.Success

        assertEquals("adam", result.data.single().login)
        assertEquals(false, result.isFromCache)
        coVerify { dao.upsertBasic(listOf(UserEntity(1, "adam", "avatar"))) }
    }

    @Test
    fun `searchUsers falls back to cache when network fails`() = runTest {
        coEvery { api.searchUsers(any(), any()) } throws IOException()
        coEvery { dao.searchByLogin("adam") } returns listOf(UserEntity(1, "adam", "avatar"))

        val result = repository.searchUsers("adam") as Resource.Success

        assertTrue(result.isFromCache)
        assertEquals(1, result.data.size)
    }

    @Test
    fun `searchUsers returns error when network fails and cache is empty`() = runTest {
        coEvery { api.searchUsers(any(), any()) } throws IOException()
        coEvery { dao.searchByLogin(any()) } returns emptyList()

        val result = repository.searchUsers("adam")

        assertTrue(result is Resource.Error)
    }

    @Test
    fun `getUserDetail caches remote result`() = runTest {
        coEvery { api.getUserDetail("adam") } returns UserDetailDto(
            id = 1, login = "adam", avatarUrl = "avatar", name = "Adam",
            publicRepos = 5, followers = 2, following = 3
        )
        coEvery { dao.upsert(any()) } just runs

        val result = repository.getUserDetail("adam") as Resource.Success

        assertEquals("Adam", result.data.name)
        assertEquals(5, result.data.publicRepos)
        coVerify { dao.upsert(match { it.isDetailCached && it.id == 1L }) }
    }

    @Test
    fun `getUserDetail ignores cached row that has no detail`() = runTest {
        coEvery { api.getUserDetail("adam") } throws IOException()
        coEvery { dao.getByLogin("adam") } returns UserEntity(1, "adam", "avatar") // basic only

        val result = repository.getUserDetail("adam")

        assertTrue(result is Resource.Error)
    }

    @Test
    fun `getUserDetail maps 404 to user not found`() = runTest {
        val notFound = HttpException(Response.error<Any>(404, "".toResponseBody()))
        coEvery { api.getUserDetail("ghost") } throws notFound
        coEvery { dao.getByLogin("ghost") } returns null

        val result = repository.getUserDetail("ghost") as Resource.Error

        assertEquals("User not found.", result.message)
    }
}
