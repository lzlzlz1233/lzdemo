package com.example.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetMovieApiService {

    @GET("trending/all/{time_window}")
    suspend fun getTrendingMovies(@Path("time_window") window: String,
                                  @Query("language") lang : String,
                                  @Query("page") page: Int = 3) : ApiResponse<MoviesApiModel>

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String, @Query("page") page: Int) : ApiResponse<MoviesApiModel>



}