package com.damtoy.githubuser.domain

sealed interface Resource<out T> {
    data class Success<T>(val data: T, val isFromCache: Boolean = false) : Resource<T>
    data class Error(val message: String) : Resource<Nothing>
}