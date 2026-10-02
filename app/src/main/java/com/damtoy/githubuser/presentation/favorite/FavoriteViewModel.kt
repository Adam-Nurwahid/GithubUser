package com.damtoy.githubuser.presentation.favorite


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.damtoy.githubuser.domain.Resource
import com.damtoy.githubuser.domain.model.User
import com.damtoy.githubuser.domain.usecase.FavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteUseCase: FavoriteUseCase
) : ViewModel() {

    private val _users =
        MutableLiveData<List<User>>(emptyList())

    val users: LiveData<List<User>> = _users

    private val _loading =
        MutableLiveData(false)

    val loading: LiveData<Boolean> = _loading

    fun loadFavorites() {

        viewModelScope.launch {

            _loading.value = true

            when (val result = favoriteUseCase.getFavorites()) {

                is Resource.Success -> {
                    _users.value = result.data
                }

                is Resource.Error -> {
                    _users.value = emptyList()
                }
            }

            _loading.value = false
        }
    }

    fun toggleFavorite(user: User) {

        viewModelScope.launch {

            when (
                favoriteUseCase.setFavorite(
                    user.id,
                    !user.isFavorite
                )
            ) {
                is Resource.Success -> loadFavorites()
                is Resource.Error -> Unit
            }
        }
    }
}