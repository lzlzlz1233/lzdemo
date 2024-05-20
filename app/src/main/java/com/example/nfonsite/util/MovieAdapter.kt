package com.example.nfonsite.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nfonsite.uiModel.FeedItem
import com.example.nfonsite.views.MovieItemView

class MovieAdapter(private val context: Context): RecyclerView.Adapter<MovieAdapter.MyViewHolder>() {

    private var list : List<FeedItem> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = when(viewType){
        ViewType.MOVIE_ITEM.ordinal -> MyViewHolder(MovieItemView(context = parent.context))
        ViewType.HEADER_ITEM.ordinal -> MyViewHolder(TextView(parent.context))
        else -> throw Exception("ViewType Not supported!")
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val view = holder.itemView
        when (view){
            is MovieItemView -> view.setup(list[position] as FeedItem.MovieItem)
        }
    }
    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int) = when(list[position]){
        is FeedItem.MovieItem -> ViewType.MOVIE_ITEM.ordinal
        is FeedItem.HeaderItem -> ViewType.HEADER_ITEM.ordinal
    }

    fun updateList(newList : List<FeedItem>){
        val diffUtil = MovieFeedDiffUtil(oldList = list, newList = newList);
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        list = newList;
        diffResult.dispatchUpdatesTo(this)
    }
    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v)

    enum class ViewType(i: Int) {
        MOVIE_ITEM(1),
        HEADER_ITEM(2),
        UNKNOWN(-1),
    }

}