package com.damtoy.githubuser.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.damtoy.githubuser.domain.Resource
import com.damtoy.githubuser.domain.model.User
import com.damtoy.githubuser.domain.usecase.FavoriteUseCase

import com.damtoy.githubuser.domain.usecase.SearchUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUsers: SearchUsersUseCase,
    private val favoriteUseCase: FavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<SearchUiState>(SearchUiState.Idle)
    val uiState: LiveData<SearchUiState> = _uiState

    private var searchJob: Job? = null
    private var lastQuery: String = ""

    /** Called on every keystroke; the request is debounced so we don't spam the API. */
    fun onQueryChanged(raw: String) {
        val query = raw.trim()
        if (query == lastQuery) return
        lastQuery = query

        searchJob?.cancel()
        if (query.isEmpty()) {
            _uiState.value = SearchUiState.Idle
            return
        }
        searchJob = viewModelScope.launch {
            delay(DEBOUNCE_MS)
            execute(query)
        }
    }

    fun retry() {
        if (lastQuery.isEmpty()) return
        searchJob?.cancel()
        searchJob = viewModelScope.launch { execute(lastQuery) }
    }

    private suspend fun execute(query: String) {
        _uiState.value = SearchUiState.Loading
        _uiState.value = when (val result = searchUsers(query)) {
            is Resource.Success ->
                if (result.data.isEmpty()) SearchUiState.Empty
                else SearchUiState.Success(result.data, result.isFromCache)
            is Resource.Error -> SearchUiState.Error(result.message)
        }
    }

    fun toggleFavorite(user: User) {
        viewModelScope.launch {

            val newValue = !user.isFavorite

            when (
                favoriteUseCase.setFavorite(
                    user.id,
                    newValue
                )
            ) {
                is Resource.Success -> {
                    val currentState = _uiState.value

                    if (currentState is SearchUiState.Success) {

                        val updatedUsers =
                            currentState.users.map {
                                if (it.id == user.id) {
                                    it.copy(
                                        isFavorite = newValue
                                    )
                                } else {
                                    it
                                }
                            }

                        _uiState.value =
                            currentState.copy(
                                users = updatedUsers
                            )
                    }
                }

                is Resource.Error -> Unit
            }
        }
    }

    companion object {
        const val DEBOUNCE_MS = 500L
    }
}
