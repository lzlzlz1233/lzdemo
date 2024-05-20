package com.example.domain.Interface

import com.example.domain.entities.DataState

interface RemoteMovieDataSource {
    suspend fun getTrendingMovies(window: String, lang : String, offset : Int = 1) : DataState
    suspend fun searchMovies(name : String, offset:Int) : DataState

}