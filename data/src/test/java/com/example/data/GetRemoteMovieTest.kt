package com.example.data

import com.example.data.datasource.RemoteMovieDataSourceImpl
import com.example.data.network.ApiResponse
import com.example.data.network.GetMovieApiService
import com.example.data.network.MoviesApiModel
import com.example.domain.entities.DataState
import com.example.domain.entities.MovieEntity
import com.example.domain.entities.Window
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetEmployeeListRemoteSourceTest {

    private val service = mock<GetMovieApiService>()
    private val datasource = RemoteMovieDataSourceImpl(service)

    @ExperimentalCoroutinesApi
    @Test
    fun `Test if data source is generating the correct Data Model for presentation Layer to use`() = runTest {
        val removeDataSource = listOf(MoviesApiModel("i12345",
            "test_title",
            "fake_back_drop",
            "fake_overview.jpg",
            "https://xxxxx.jpg",
        ))
        val expetedRemployees = DataState(
            items = listOf(MovieEntity("i12345", "test_title", "fake_back_drop","https://xxxxx.jpg", "fake_overview.jpg")),
            page = 1,
            canLoadMore = true
        )
        val apiRes = ApiResponse<MoviesApiModel>().apply {
            data = removeDataSource
            total_page = 3
            page = 1}
        whenever(service.getTrendingMovies(Window.day.name, "en-US", 1)).thenReturn(apiRes)
        val result = datasource.getTrendingMovies(Window.day.name, "en-US", 1)
        Assert.assertEquals(expetedRemployees, result)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test if data source is generating the correct Data Model for presentation Layer to use when we cannot load more`() = runTest {
        val removeDataSource = listOf(MoviesApiModel("i12345",
            "test_title",
            "fake_back_drop",
            "fake_overview.jpg",
            "https://xxxxx.jpg",
        ))
        val expetedRemployees = DataState(
            items = listOf(MovieEntity("i12345", "test_title", "fake_back_drop","https://xxxxx.jpg", "fake_overview.jpg")),
            page = 1,
            canLoadMore = false
        )
        val apiRes = ApiResponse<MoviesApiModel>().apply {
            data = removeDataSource
            total_page = 1
            page = 1}
        whenever(service.getTrendingMovies(Window.day.name, "en-US", 1)).thenReturn(apiRes)
        val result = datasource.getTrendingMovies(Window.day.name, "en-US", 1)
        Assert.assertEquals(expetedRemployees, result)
    }
}