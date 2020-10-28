package com.example.movies.paging

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.model.MovieModel

class MoviesAdapter : PagingDataAdapter<MovieModel, RecyclerView.ViewHolder>(MOVIE_COMPARATOR) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return MovieViewHolder.create(parent)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val movieItem = getItem(position)
        if (movieItem != null) {
            (holder as MovieViewHolder).bind(movieItem)
        }
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<MovieModel>() {
            override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
                oldItem == newItem
        }
    }
}
