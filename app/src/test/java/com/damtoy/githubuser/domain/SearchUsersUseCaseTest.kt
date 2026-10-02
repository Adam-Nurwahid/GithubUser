package com.damtoy.githubuser.domain


import com.damtoy.githubuser.domain.model.User
import com.damtoy.githubuser.domain.repository.UserRepository
import com.damtoy.githubuser.domain.usecase.SearchUsersUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchUsersUseCaseTest {

    private val repository = mockk<UserRepository>()
    private val useCase = SearchUsersUseCase(repository)

    @Test
    fun `blank query returns empty list without hitting repository`() = runTest {
        val result = useCase("   ")

        assertEquals(Resource.Success(emptyList<User>()), result)
        coVerify(exactly = 0) { repository.searchUsers(any()) }
    }

    @Test
    fun `query is trimmed before being passed to repository`() = runTest {
        val users = listOf(User(1, "adam", "url"))
        coEvery { repository.searchUsers("adam") } returns Resource.Success(users)

        val result = useCase("  adam ")

        assertEquals(Resource.Success(users), result)
    }
}
