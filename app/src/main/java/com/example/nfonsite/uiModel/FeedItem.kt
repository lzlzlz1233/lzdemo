package com.example.nfonsite.uiModel


/**
 * General feed item type used by recycler view inside [MainFragment]
 */
sealed class FeedItem(val id: String){

    val type : String = (this:: class.java).simpleName
    class MovieItem(
        val movie : MovieItemSpec
    ): FeedItem (
        id = movie.id
    )

    class HeaderItem(
        val headerText : String
    ): FeedItem (
        id = headerText.hashCode().toString()
    )
}