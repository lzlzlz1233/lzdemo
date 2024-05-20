package com.example.domain.entities

data class MovieEntity(
    val id: String,
    val name: String,
    val backDropPath: String,
    val posterPath: String,
    val overview: String ?= null,
    val page: Int = 1,
    )


data class DataState(
    val items: List<MovieEntity>,
    val page :Int,
    val canLoadMore :Boolean
)