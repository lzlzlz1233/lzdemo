package com.example.data.datasource

import com.example.data.network.GetMovieApiService
import com.example.data.network.MoviesApiModel
import com.example.domain.Interface.RemoteMovieDataSource
import com.example.domain.entities.MovieEntity
import javax.inject.Inject

class RemoteMovieDataSourceImpl @Inject constructor( val apiService: GetMovieApiService): RemoteMovieDataSource {

    override suspend fun getTrendingMovies(window: String, lang : String): List<MovieEntity>  {
        val movies = apiService.getTrendingMovies(window, lang).data?.map {
            convertToMovieEntity(it)
        }?: run{
            emptyList()
        }
        return movies
    }

    override suspend fun searchMovies(name: String): List<MovieEntity>  {
        val movies = apiService.searchMovies(name, 1).data?.map {
            convertToMovieEntity(it)
        }?: run{
            emptyList()
        }
        return movies
    }
    private fun convertToMovieEntity(rawApiModel : MoviesApiModel ) =
        MovieEntity(
            id = rawApiModel.id,
            name = rawApiModel.name,
            backDropPath = rawApiModel.backDropPath?:"NA",
            posterPath = rawApiModel.posterPath?:"NA",
        )

}