package com.example.data.datasource

import com.example.data.network.GetMovieApiService
import com.example.data.network.MoviesApiModel
import com.example.domain.Interface.RemoteMovieDataSource
import com.example.domain.entities.DataState
import com.example.domain.entities.MovieEntity
import javax.inject.Inject

class RemoteMovieDataSourceImpl @Inject constructor(val apiService: GetMovieApiService) :
    RemoteMovieDataSource {

    override suspend fun getTrendingMovies(window: String, lang: String, offset: Int): DataState {
        val result = apiService.getTrendingMovies(window, lang, offset)
        val movies = result.data?.map {
            convertToMovieEntity(it, result.page)
        } ?: run {
            emptyList()
        }
        return convertToDataState(movies, result.page, result.total_page)
    }

    override suspend fun searchMovies(name: String, offset: Int): DataState {
        val result = apiService.searchMovies(name, offset)
        val movies = result.data?.map {
            convertToMovieEntity(it, result.page)
        } ?: run {
            emptyList()
        }
        return convertToDataState(movies, result.page, result.total_page)
    }

    private fun convertToMovieEntity(rawApiModel: MoviesApiModel, page: Int) =
        MovieEntity(
            id = rawApiModel.id,
            name = rawApiModel.name,
            backDropPath = rawApiModel.backDropPath ?: "NA",
            posterPath = rawApiModel.posterPath ?: "NA",
            page = page
        )

    private fun convertToDataState(items: List<MovieEntity>, page: Int, totalPage: Int) =
        DataState(items, page, page < totalPage)

}