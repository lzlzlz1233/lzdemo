package com.example.nfonsite.util

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.MutableLiveData
import com.example.domain.entities.DataState
import com.example.domain.entities.ErrorType
import com.example.domain.entities.MovieEntity
import java.io.Serializable

import com.example.domain.entities.Result
import com.example.nfonsite.MovieViewModel
import com.example.nfonsite.uiModel.FeedItem
import com.example.nfonsite.uiModel.MovieItemSpec

/**
 * extension func to convert [Result] to [UiState]
 */
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

/**
 * extension func to convert [MovieEntity] to [FeedItem]
 */
fun MovieEntity.toFeedItem() = FeedItem.MovieItem(
    movie = MovieItemSpec(
        id = this.id,
        name = this.name,
        imgPath = this.backDropPath,
        posterPath = this.posterPath,
        overView = this.overview
    )
)

/***
 * Business logic reusable to transform Result into UIState
 */
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
fun Result<*>.applyToState(
    originalList: MutableList<FeedItem>,
    data: MutableLiveData<UiState<MovieViewModel.ScreenContent>>,
    query: String? = "",
    onSaveState: (UiState<MovieViewModel.ScreenContent>) -> Unit
) {
    when (this) {
        is Result.Success -> {
            if (this.data is DataState) {
                val newMovieList =
                    (this.data as DataState).items.map {
                        it.toFeedItem()
                    }
                originalList.addAll(newMovieList)
                data.value =
                    this.convertToUiState {
                        MovieViewModel.ScreenContent(
                            items = originalList,
                            query = query,
                            canLoadMore = (this.data as DataState).canLoadMore,
                            nextOffset = (this.data as DataState).page + 1
                        )
                    }
                onSaveState(data.value!!)
            } else {
                UiState.Error(ErrorType.OTHER.name)
            }
        }

        is Result.Error -> data.value = UiState.Error(this.errorMessage)
    }
}