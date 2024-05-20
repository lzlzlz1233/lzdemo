package com.example.data.network

import android.view.Window
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GetMovieApiService {

    @GET("trending/all/{time_window}")
    suspend fun getTrendingMovies(@Path("time_window") window: String, @Query("language") lang : String) : ApiResponse<MoviesApiModel>

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String, @Query("page") page: Int) : ApiResponse<MoviesApiModel>



}