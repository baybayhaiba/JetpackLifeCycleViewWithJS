package com.example.jetpacklifecycleviewwithjs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()


    @Composable
    fun <T> HandleResult(
        loadingContent: @Composable () -> Unit,
        successContent: @Composable (data: T) -> Unit,
        errorContent: @Composable (errorMessage: String) -> Unit,
        modifier: Modifier = Modifier
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
