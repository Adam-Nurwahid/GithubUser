package com.damtoy.githubuser.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.damtoy.githubuser.domain.Resource

import com.damtoy.githubuser.domain.usecase.SearchUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUsers: SearchUsersUseCase
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

    companion object {
        const val DEBOUNCE_MS = 500L
    }
}
