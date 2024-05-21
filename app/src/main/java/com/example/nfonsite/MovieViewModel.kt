package com.example.nfonsite

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entities.Window
import com.example.domain.repository.RemoteMovieRepository
import com.example.nfonsite.uiModel.FeedItem
import com.example.nfonsite.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.nfonsite.util.applyToState

/**
 * Core View Model used by [MainFragment] for movie list rendering
 */
@HiltViewModel
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class MovieViewModel @Inject constructor(
    private val repository: RemoteMovieRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // source list of all the items inside the fragment
    private var _movieList: MutableList<FeedItem> = mutableListOf()

    private var _data: MutableLiveData<UiState<ScreenContent>> = MutableLiveData()
    val data: LiveData<UiState<ScreenContent>> get() = _data


    /**
     * Public function consumed by Fragment to get trending movies
     * @param query: string query
     */
    fun getList() {
        getSavedData()
        if (shouldReload() || data.value is UiState.Empty || data.value is UiState.Error) {
            getMoviesFromRemoteSource(DEFAULT_TENDING, lan = DEFAULT_LOCALE)
        }
    }

    /**
     * Public function consumed by Fragment to search given user input
     * @param query: string query
     */
    fun search(query: String) {
        if (query.isEmpty()) {
            clear()
            getMoviesFromRemoteSource(window = DEFAULT_TENDING, lan = DEFAULT_LOCALE)
        } else {
            searchMovie(query)
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun searchMovie(query: String) {
        if (!shouldLoadNewItems()) return
        /*
        if this query is new, then we need to clear the current list and re-render
         */
        if (isNewQuery(query)) clear()
        val offset = getCurrentOffset()
        _data.value = UiState.Loading
        viewModelScope.launch {
            repository.searchMovies(query, offset).applyToState(
                originalList = _movieList,
                query = query, data = _data, onSaveState = ::saveMovieState
            )
        }
    }

    /**
     * Get Data from remote source using [RemoteMovieRepository]
     */
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun getMoviesFromRemoteSource(window: Window, lan: String) {
        if (!shouldLoadNewItems()) return
        val offset = getCurrentOffset()
        _data.value = UiState.Loading
        viewModelScope.launch {
            repository.getMovies(window, lan, offset).applyToState(
                originalList = _movieList, data = _data, onSaveState = ::saveMovieState
            )
        }
    }


    private fun updateSearchQuery(query: String) {
        when (_data.value) {
            is UiState.Success -> {
                (_data.value as UiState.Success<ScreenContent>).data.query = query
            }
            else -> Unit
        }
    }

    /**
     * return if the current query user generate is the same as the previous one
     */
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

    /**
     * get data from state handle
     */
    private fun getSavedData() {
        val savedData: UiState<ScreenContent>? =
            savedStateHandle.getLiveData<UiState<ScreenContent>>(STATE_KEY).value
        _data.value = savedData ?: UiState.Empty
    }

    fun saveMovieState(item: UiState<ScreenContent>) {
        savedStateHandle[STATE_KEY] = item
    }

    fun refresh() {
        clear()
        getMoviesFromRemoteSource(DEFAULT_TENDING, lan = DEFAULT_LOCALE)

    }

    fun loadMore() {
        if (hasQuery()) {
            (_data.value as UiState.Success).data.query?.let {
                searchMovie(it)
            }
        } else {
            getMoviesFromRemoteSource(DEFAULT_TENDING, lan = DEFAULT_LOCALE)
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

    private fun shouldReload(): Boolean {
        return when (data.value) {
            is UiState.Success -> {
                return (data.value as UiState.Success<ScreenContent>).data.items.isEmpty()
            }
            else -> true
        }
    }

    data class ScreenContent(
        val items: List<FeedItem> = emptyList(),
        var query: String? = null,
        var nextOffset: Int = 1,
        val canLoadMore: Boolean = true
    )

    companion object {
        val STATE_KEY = "saved_state"
        val DEFAULT_TENDING = Window.day
        val DEFAULT_LOCALE= "en-US"

    }


}