package com.example.nfonsite

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entities.DataState
import com.example.domain.entities.ErrorType
import com.example.domain.entities.MovieEntity
import com.example.domain.entities.Window
import com.example.domain.repository.RemoteMovieRepository
import com.example.nfonsite.uiModel.FeedItem
import com.example.nfonsite.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.domain.entities.Result
import com.example.nfonsite.util.convertToUiState
import com.example.nfonsite.util.toHeaderItem
import java.io.Serializable

/**
 * Core View Model used by [MainFragment] for movie list rendering
 */
@HiltViewModel
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class MovieViewModel @Inject constructor(
    private val repository: RemoteMovieRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _movieList: MutableList<FeedItem> = mutableListOf()

    private var _data: MutableLiveData<UiState<ScreenContent>> = MutableLiveData()
    val data: LiveData<UiState<ScreenContent>> get() = _data
    private fun shouldReload(): Boolean {
        return when (data.value) {
            is UiState.Success -> {
                return (data.value as UiState.Success<ScreenContent>).data.items.isEmpty()
            }

            UiState.Empty -> true
            is UiState.Error -> true
            UiState.Loading -> true
            null -> true
        }
    }

    fun getSavedData() {
        val savedData: UiState<ScreenContent>? =
            savedStateHandle.getLiveData<UiState<ScreenContent>>(STATE_KEY).value
        _data.value = savedData ?: UiState.Empty
    }

    fun getList() {
        getSavedData()
        if (shouldReload() || data.value is UiState.Empty || data.value is UiState.Error) {
            getEmployeesFromRemoteSource(Window.day, lan = "en-US")
        }
    }

    fun updateSearchQuery(query: String) {
        when (_data.value) {
            is UiState.Success -> {
                (_data.value as UiState.Success<ScreenContent>).data.query = query
            }

            else -> Unit
        }
    }

    fun search(query: String) {
        if (query.isEmpty()) {
            clear()
            getEmployeesFromRemoteSource(Window.day, lan = "en-US")
        }else{
            searchMovie(query)
        }
    }

    /**
     * Get Data from remote source using [RemoteMovieRepository]
     */
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun getEmployeesFromRemoteSource(window: Window, lan: String) {
        // if ui is already loading , we should not perform api call again
        if (_data.value == UiState.Loading) return
        if (_data.value is UiState.Success && !(_data.value as UiState.Success).data.canLoadMore) return

        var offset = 1
        if (_data.value is UiState.Success) {
            (_data.value as UiState.Success).apply {
                offset = this.data.nextOffset
            }
        }
        _data.value = UiState.Loading
        viewModelScope.launch {
            repository.getMovies(window, lan, offset).apply {
                when (this) {
                    is Result.Success -> {
                        if (this.data is DataState) {
                            val newMovieList =
                                (this.data as DataState).items.map {
                                    it.toHeaderItem()
                                }
                            _movieList.addAll(newMovieList)
                            _data.value =
                                this.convertToUiState {
                                    ScreenContent(
                                        items = _movieList,
                                        query = "",
                                        canLoadMore = (this.data as DataState).canLoadMore,
                                        nextOffset = (this.data as DataState).page + 1
                                    )
                                }
                            saveMovieState(_data.value!!)
                            // adding sharedPref here if necessary
                        } else {
                            UiState.Error(ErrorType.OTHER.name)
                        }
                    }

                    is Result.Error -> _data.value = UiState.Error(this.errorMessage)
                }
            }
        }
    }

    private fun isNewQuery(q: String): Boolean {
        return when (_data.value) {
            is UiState.Success -> {
                val ifNew = !(_data.value as UiState.Success).data.query.equals(q)
                if (ifNew) updateSearchQuery(query = q)
                return ifNew
            }
            else -> true
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun searchMovie(query: String) {
        // if ui is already loading , we should not perform api call again
        if (_data.value == UiState.Loading) return
        if (_data.value is UiState.Success && !(_data.value as UiState.Success).data.canLoadMore) return
        var offset = 1
        if (_data.value is UiState.Success) {
            (_data.value as UiState.Success).apply {
                offset = this.data.nextOffset
            }
        }
        if (isNewQuery(query)) clear()
        _data.value = UiState.Loading
        viewModelScope.launch {
            repository.searchMovies(query, offset).apply {
                when (this) {
                    is Result.Success -> {
                        if (this.data is DataState) {
                            val newMovieList =
                                (this.data as DataState).items.map {
                                    it.toHeaderItem()
                                }
                            _movieList.addAll(newMovieList)
                            _data.value =
                                this.convertToUiState {
                                    ScreenContent(
                                        items = _movieList,
                                        query = query,
                                        canLoadMore = (this.data as DataState).canLoadMore,
                                        nextOffset = (this.data as DataState).page + 1
                                    )
                                }
                            saveMovieState(_data.value!!)
                        } else {
                            UiState.Error(ErrorType.OTHER.name)
                        }
                    }

                    is Result.Error -> _data.value = UiState.Error(this.errorMessage)
                }
            }
        }
    }

    fun saveMovieState(item: UiState<ScreenContent>) {
        savedStateHandle[STATE_KEY] = item
    }

    fun refresh() {
        clear()
        getEmployeesFromRemoteSource(Window.day, lan = "en-US")

    }

    fun loadMore() {
        if (hasQuery()) {
            (_data.value as UiState.Success).data.query?.let {
                searchMovie(it)
            }
        } else {
            getEmployeesFromRemoteSource(Window.day, lan = "en-US")
        }
    }

    fun clear() {
        _movieList.clear()
        _data.value = UiState.Empty
    }

    fun hasQuery() = when (_data.value) {
        is UiState.Success -> !(_data.value as UiState.Success).data.query.isNullOrEmpty()
        else -> false
    }

    fun canLoadMore() = _data.value is UiState.Success

    companion object {
        val STATE_KEY = "saved_state"
    }


    data class ScreenContent(
        val items: List<FeedItem> = emptyList(),
        var query: String? = null,
        var nextOffset: Int = 1,
        val canLoadMore: Boolean = true
    )


}