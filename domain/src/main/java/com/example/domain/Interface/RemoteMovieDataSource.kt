package com.example.domain.Interface

import com.example.domain.entities.MovieEntity

interface RemoteMovieDataSource {
    suspend fun getTrendingMovies(window: String, lang : String) : List<MovieEntity>
    suspend fun searchMovies(name : String) : MovieEntity

}