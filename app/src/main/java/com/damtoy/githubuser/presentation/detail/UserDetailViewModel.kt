package com.damtoy.githubuser.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.damtoy.githubuser.domain.Resource
import com.damtoy.githubuser.domain.model.UserDetail
import com.damtoy.githubuser.domain.usecase.FavoriteUseCase
import com.damtoy.githubuser.domain.usecase.GetUserDetailUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserDetail: GetUserDetailUseCase,
    private val favoriteUseCase: FavoriteUseCase
) : ViewModel() {

    // Navigation arguments are automatically exposed through SavedStateHandle.
    private val username: String = checkNotNull(savedStateHandle.get<String>(ARG_USERNAME))

    private val _uiState = MutableLiveData<DetailUiState>(DetailUiState.Loading)
    val uiState: LiveData<DetailUiState> = _uiState

    init {
        load()
    }

    fun load() {
        _uiState.value = DetailUiState.Loading
        viewModelScope.launch {
            _uiState.value = when (val result = getUserDetail(username)) {
                is Resource.Success -> DetailUiState.Success(result.data, result.isFromCache)
                is Resource.Error -> DetailUiState.Error(result.message)
            }
        }
    }

    fun toggleFavorite(detail: UserDetail) {

        viewModelScope.launch {

            when (
                favoriteUseCase.setFavorite(
                    detail.id,
                    !detail.isFavorite
                )
            ) {
                is Resource.Success -> load()
                is Resource.Error -> Unit
            }
        }
    }
    companion object {
        const val ARG_USERNAME = "username"
    }
}
