package com.example.nfonsite.uiModel


/**
 * UI Model spec for MovieItem used by recycler view inside [MainFragment]
 */
data class MovieItemSpec (
    val id : String,
    val name :String?=null,
    val imgPath :String?=null,
    val overView :String?=null,
    val posterPath :String?=null
)