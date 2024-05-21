package com.example.nfonsite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.domain.entities.DataState
import com.example.domain.entities.MovieEntity
import com.example.domain.entities.Window
import com.example.domain.repository.RemoteMovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import com.example.domain.entities.Result
import com.example.nfonsite.util.UiState
import com.example.nfonsite.util.toFeedItem
import kotlinx.coroutines.test.runTest
import org.junit.Assert

class ViewModelTest {

    private val stateHandle = SavedStateHandle()

    private val repositoryImpl = mock<RemoteMovieRepository>()
    private var viewModel: MovieViewModel = MovieViewModel( repositoryImpl , stateHandle)

    @get:Rule
    val rule = InstantTaskExecutorRule()

    val dispatcher = TestCoroutineDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `check if ViewModel is rendering the expected viewState for getTrendingMovies`() = runTest{
        val dataState = DataState(
            items = listOf(MovieEntity("i12345", "test_title", "fake_back_drop","https://xxxxx.jpg", "fake_overview.jpg")),
            page = 1,
            canLoadMore = true
        )
        whenever(repositoryImpl.getMovies(Window.day, "en-US", 1)).thenReturn(Result.Success(dataState))
        val expected = UiState.Success(MovieViewModel.ScreenContent(
            items = dataState.items.map { it.toFeedItem() },
            query = "",
            nextOffset = 2,
            canLoadMore = true

        ))
        viewModel.getList()
        (viewModel.data.value as UiState.Success).data.items.forEachIndexed { index, feedItem ->
            Assert.assertEquals(feedItem.id, expected.data.items[index].id)

        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `check if ViewModel is rendering the expected viewState for getSearch`() = runTest{
        val dataState = DataState(
            items = listOf(MovieEntity("i12345", "test_title", "fake_back_drop","https://xxxxx.jpg", "fake_overview.jpg")),
            page = 1,
            canLoadMore = true
        )
        whenever(repositoryImpl.searchMovies("test_query", 1)).thenReturn(Result.Success(dataState))
        val expected = UiState.Success(MovieViewModel.ScreenContent(
            items = dataState.items.map { it.toFeedItem() },
            query = "test_query",
            nextOffset = 2,
            canLoadMore = true
        ))
        viewModel.search("test_query")
        (viewModel.data.value as UiState.Success).data.items.forEachIndexed { index, feedItem ->
            Assert.assertEquals(feedItem.id, expected.data.items[index].id)

        }
    }
}