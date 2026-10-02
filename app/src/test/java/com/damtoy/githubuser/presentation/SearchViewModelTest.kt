package com.damtoy.githubuser.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.damtoy.githubuser.domain.Resource
import com.damtoy.githubuser.domain.model.User
import com.damtoy.githubuser.domain.usecase.SearchUsersUseCase
import com.damtoy.githubuser.presentation.search.SearchUiState
import com.damtoy.githubuser.presentation.search.SearchViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.damtoy.githubuser.util.MainDispatcherRule

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule val instantTaskRule = InstantTaskExecutorRule()
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    private val searchUsers = mockk<SearchUsersUseCase>()
    private lateinit var viewModel: SearchViewModel

    private val user = User(1, "adam", "avatar")

    @Before
    fun setUp() {
        viewModel = SearchViewModel(searchUsers)
    }

    @Test
    fun `initial state is Idle`() {
        assertEquals(SearchUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `success result is emitted after debounce`() = runTest {
        coEvery { searchUsers("adam") } returns Resource.Success(listOf(user))

        viewModel.onQueryChanged("adam")
        advanceTimeBy(SearchViewModel.DEBOUNCE_MS - 1)
        assertEquals(SearchUiState.Idle, viewModel.uiState.value) // still debouncing

        advanceUntilIdle()
        assertEquals(SearchUiState.Success(listOf(user), false), viewModel.uiState.value)
    }

    @Test
    fun `rapid typing only triggers one request with the last query`() = runTest {
        coEvery { searchUsers(any()) } returns Resource.Success(listOf(user))

        viewModel.onQueryChanged("a")
        advanceTimeBy(100)
        viewModel.onQueryChanged("ad")
        advanceTimeBy(100)
        viewModel.onQueryChanged("adam")
        advanceUntilIdle()

        coVerify(exactly = 1) { searchUsers(any()) }
        coVerify(exactly = 1) { searchUsers("adam") }
    }

    @Test
    fun `empty result emits Empty`() = runTest {
        coEvery { searchUsers("zzz") } returns Resource.Success(emptyList())

        viewModel.onQueryChanged("zzz")
        advanceUntilIdle()

        assertEquals(SearchUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun `error result emits Error and retry can recover`() = runTest {
        coEvery { searchUsers("adam") } returns Resource.Error("boom")
        viewModel.onQueryChanged("adam")
        advanceUntilIdle()
        assertEquals(SearchUiState.Error("boom"), viewModel.uiState.value)

        coEvery { searchUsers("adam") } returns Resource.Success(listOf(user), isFromCache = true)
        viewModel.retry()
        advanceUntilIdle()
        assertEquals(SearchUiState.Success(listOf(user), true), viewModel.uiState.value)
    }

    @Test
    fun `clearing the query goes back to Idle`() = runTest {
        coEvery { searchUsers("adam") } returns Resource.Success(listOf(user))
        viewModel.onQueryChanged("adam")
        advanceUntilIdle()

        viewModel.onQueryChanged("")

        assertEquals(SearchUiState.Idle, viewModel.uiState.value)
    }
}
