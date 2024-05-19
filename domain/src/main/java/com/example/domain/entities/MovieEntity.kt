package com.example.domain.entities

data class MovieEntity(
    val id: String,
    val name: String,
    val backDropPath: String,
    val posterPath: String,
    val overview: String ?= null,
    )