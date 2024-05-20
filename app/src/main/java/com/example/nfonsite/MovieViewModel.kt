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
import com.example.domain.entities.Window
import com.example.domain.repository.RemoteMovieRepository
import com.example.nfonsite.uiModel.FeedItem
import com.example.nfonsite.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.domain.entities.Result
import com.example.nfonsite.util.applyToState
import com.example.nfonsite.util.convertToUiState
import com.example.nfonsite.util.toHeaderItem

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
        } else {
            searchMovie(query)
        }
    }

    /**
     * Get Data from remote source using [RemoteMovieRepository]
     */
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun getEmployeesFromRemoteSource(window: Window, lan: String) {
        if (!shouldLoadNewItems()) return
        _data.value = UiState.Loading
        viewModelScope.launch {
            repository.getMovies(window, lan, getCurrentOffset()).applyToState(
                originalList = _movieList, data = _data, onSaveState = ::saveMovieState
            )
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

    private fun shouldLoadNewItems(): Boolean {
        // if ui is already loading , we should not perform api call again
        if (_data.value is UiState.Loading) return false
        if (_data.value is UiState.Success && !(_data.value as UiState.Success).data.canLoadMore) return false
        return true
    }

    private fun getCurrentOffset() = when (_data.value) {
        is UiState.Success -> (_data.value as UiState.Success).data.nextOffset
        else -> 1
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun searchMovie(query: String) {
        if (!shouldLoadNewItems()) return
        if (isNewQuery(query)) clear()
        _data.value = UiState.Loading
        viewModelScope.launch {
            repository.searchMovies(query, getCurrentOffset()).applyToState(
                originalList = _movieList,
                query = query, data = _data, onSaveState = ::saveMovieState
            )
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

    fun canLoadMore() = _data.value is UiState.Success || _data.value is UiState.Empty

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