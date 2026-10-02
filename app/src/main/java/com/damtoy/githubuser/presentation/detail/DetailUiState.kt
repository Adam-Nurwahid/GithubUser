package com.damtoy.githubuser.presentation.detail

import com.damtoy.githubuser.domain.model.UserDetail


sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(val detail: UserDetail, val isFromCache: Boolean) : DetailUiState
    data class Error(val message: String) : DetailUiState
}