package com.damtoy.githubuser.presentation.search

import com.damtoy.githubuser.domain.model.User


sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data object Empty : SearchUiState
    data class Success(val users: List<User>, val isFromCache: Boolean) : SearchUiState
    data class Error(val message: String) : SearchUiState
}