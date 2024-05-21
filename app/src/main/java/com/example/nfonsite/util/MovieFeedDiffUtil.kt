package com.example.nfonsite.util

import androidx.recyclerview.widget.DiffUtil
import com.example.nfonsite.uiModel.FeedItem

class MovieFeedDiffUtil(
    private val oldList : List<FeedItem>,
    private val newList: List<FeedItem>

) : DiffUtil.Callback(){
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        (oldList[oldItemPosition].type == newList[newItemPosition].type &&
                oldList[oldItemPosition].id == newList[newItemPosition].id)

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            (oldList[oldItemPosition].type != newList[newItemPosition].type ||
                    oldList[oldItemPosition].id != newList[newItemPosition].id)
            -> false
            else -> true
        }
    }

}