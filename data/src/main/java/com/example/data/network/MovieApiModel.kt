package com.example.data.network

import com.squareup.moshi.Json


data class ApiResponse<T: Any> (
    @Json(name = "page") var page : Int = 0,
    @Json(name = "total_pages") var total_page : Int = 0,
    @Json(name = "results") var data : List<T> ?= null,
)

data class MoviesApiModel(
    @Json(name = "id") val id: String,
    @Json(name = "title") val name: String ="unknown",
    @Json(name = "backdrop_path") val backDropPath: String? = "NA",
    @Json(name = "overview") val overview: String ?= null,
    @Json(name = "poster_path") val posterPath: String? = "NA"
)