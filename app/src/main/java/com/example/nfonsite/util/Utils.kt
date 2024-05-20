package com.example.nfonsite.util

import com.example.domain.entities.MovieEntity
import java.io.Serializable

import com.example.domain.entities.Result
import com.example.nfonsite.uiModel.FeedItem
import com.example.nfonsite.uiModel.MovieItemSpec

fun <T : Any, R : Any> Result<T>.convertToUiState(converter: (T) -> R): UiState<R> {
    return when (this) {
        is Result.Error -> UiState.Error(errorMessage)
        is Result.Success -> UiState.Success(converter(data))
    }
}

sealed class UiState<out T : Any> {
    data object Loading : UiState<Nothing>()
    data object Empty : UiState<Nothing>()
    data class Error(val errorMessage: String?) : UiState<Nothing>(), Serializable
    data class Success<T : Any>(val data: T) : UiState<T>(), Serializable
}

fun MovieEntity.toHeaderItem() = FeedItem.MovieItem(movie = MovieItemSpec(id = this.id, imgPath = this.posterPath))