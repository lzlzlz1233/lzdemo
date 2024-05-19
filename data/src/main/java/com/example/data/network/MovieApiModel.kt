package com.example.data.network

import com.squareup.moshi.Json


data class ApiResponse<T: Any> (
    @Json(name = "page") val page : Int = 0,
    @Json(name = "movies") var data : List<T> ?= null,
)

data class MoviesApiModel(
    @Json(name = "id") val id: String,
    @Json(name = "title") val name: String,
    @Json(name = "backdrop_path") val backDropPath: String,
    @Json(name = "overview") val overview: String ?= null,
    @Json(name = "poster_path") val posterPath: String
)