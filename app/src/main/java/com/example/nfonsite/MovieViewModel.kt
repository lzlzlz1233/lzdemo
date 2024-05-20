package com.example.nfonsite

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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


    fun getList() {
        getEmployeesFromRemoteSource(Window.day, lan = "en-US")
    }

    fun search(query : String){
        searchMovie(query)
    }
    /**
     * Get Data from remote source using [RemoteMovieRepository]
     */
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun getEmployeesFromRemoteSource(window : Window, lan : String) {
        // if ui is already loading , we should not perform api call again
        if (_data.value == UiState.Loading) return
        _data.value = UiState.Loading
        viewModelScope.launch {
            repository.getMovies(window, lan).apply {
                when (this) {
                    is Result.Success -> {
                        if (this.data is List<*>) {
                            val newMovieList =
                                (this.data as List<*>).filterIsInstance<MovieEntity>().map {
                                    it.toHeaderItem()
                                }
                            _data.value =
                                this.convertToUiState { ScreenContent(items = newMovieList) }
                            // save the raw copy into the list
                            _movieList.clear()
                            _movieList.addAll(newMovieList)
//                            saveEmployeeList(_data.value!!)
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


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun searchMovie(query : String) {
        // if ui is already loading , we should not perform api call again
        if (_data.value == UiState.Loading) return
        _data.value = UiState.Loading
        viewModelScope.launch {
            repository.searchMovies(query).apply {
                when (this) {
                    is Result.Success -> {
                        if (this.data is List<*>) {
                            val newMovieList =
                                (this.data as List<*>).filterIsInstance<MovieEntity>().map {
                                    it.toHeaderItem()
                                }
                            _data.value =
                                this.convertToUiState { ScreenContent(items = newMovieList) }
                            // save the raw copy into the list
                            _movieList.clear()
                            _movieList.addAll(newMovieList)
//                            saveEmployeeList(_data.value!!)
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




    data class ScreenContent(
        val items: List<FeedItem> = emptyList(),
    )





}