package com.example.jetpacklifecycleviewwithjs

import androidx.compose.runtime.Composable

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()


    @Composable
    fun <T> toUi(
        loadingContent: @Composable () -> Unit,
        successContent: @Composable (data: T) -> Unit,
        errorContent: @Composable (errorMessage: String) -> Unit,
    ) {
        when (this) {
            is Loading -> {
                loadingContent()
            }

            is Success -> {
                successContent(this.data as T)
            }

            is Error -> {
                errorContent(this.message)
            }
        }
    }


}
