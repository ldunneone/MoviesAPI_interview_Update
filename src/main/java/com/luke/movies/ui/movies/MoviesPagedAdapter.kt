package com.luke.movies.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.luke.movies.BuildConfig
import com.luke.movies.data.models.movies.Movie
import com.luke.movies.databinding.ItemMovieBinding
import com.luke.movies.helpers.extensions.loadImage
import kotlinx.android.synthetic.main.item_movie.view.*

class MoviesPagedAdapter: PagingDataAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    var onMovieClicked: ((Movie?) -> Unit)? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MovieItemViewHolder).bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieItemViewHolder(ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            (oldItem.id == newItem.id)

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            (oldItem == newItem)
    }

    inner class MovieItemViewHolder(view: ItemMovieBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(movie: Movie?) {
            itemView.title.text = movie?.title
            itemView.release_date.text = movie?.releaseDate
            val voteAverage: Double? = movie?.voteAverage
            itemView.circularProgressBar.progress = voteAverage?.times(10)?.toFloat()!!
            itemView.percentage.text = String.format("%d%s", voteAverage.times(10).toInt(), "%")
            itemView.image.loadImage("${BuildConfig.POSTER_BASE_URL}${movie.posterPath}")
            itemView.setOnClickListener {
                onMovieClicked?.invoke(movie)
            }
        }
    }
}