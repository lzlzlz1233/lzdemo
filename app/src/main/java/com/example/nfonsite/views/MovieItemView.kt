package com.example.nfonsite.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.nfonsite.R
import com.example.nfonsite.databinding.MovieItemViewBinding
import com.example.nfonsite.uiModel.FeedItem

/**
 * Customized View responsible to render images inside the grid
 */
class MovieItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: MovieItemViewBinding =
        MovieItemViewBinding.inflate(LayoutInflater.from(context), this, true)

    @SuppressLint("CheckResult")
    fun setup(item: FeedItem.MovieItem, onClick: (FeedItem) -> Unit) {
        /*
     Ensure all memory and disk caches are used in Glide
      */
        val options = RequestOptions().apply {
            this.skipMemoryCache(false)
            this.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        }
        this.setOnClickListener {
            onClick(item)
        }
        val url ="https://image.tmdb.org/t/p/w500" + item.movie.posterPath
        val width = resources.displayMetrics.widthPixels /3
        Glide.with(context)
            .load(url)
            .apply(options)
            .placeholder(R.drawable.ic_launcher_foreground)
            .override(350,500)
            .into(binding.movieImage)
    }

}