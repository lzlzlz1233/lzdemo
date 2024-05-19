package com.example.data.network

import android.view.Window
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetMovieApiService {

    @GET("trending/all/{time_window}")
    suspend fun getTrendingMovies(@Path("time_window") window: String, @Query("language") lang : String) : ApiResponse<MoviesApiModel>



}