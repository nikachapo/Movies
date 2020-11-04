package com.example.movies.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.databinding.ItemMovieBinding
import com.example.movies.model.MovieModel
import com.example.movies.utils.loadImage

class MovieViewHolder(
    private val binding: ItemMovieBinding,
    private val onItemClick: (MovieModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var movie: MovieModel? = null
    private val view = binding.root

    fun bind(movie: MovieModel) {
        showMovieData(movie)
    }

    private fun showMovieData(movie: MovieModel) {
        this.movie = movie
        binding.itemCardView.rotationY =
            if (bindingAdapterPosition % 2 == 0) (-6).toFloat() else 6.toFloat()
        binding.itemNameTV.text = movie.name
        binding.itemPosterIV.loadImage(movie.posterUrl)
        binding.itemRatingTv.text = movie.voteAverage.toString()
        view.setOnClickListener {
            onItemClick(movie)
        }
    }

    companion object {
        fun create(parent: ViewGroup, onItemClick: (MovieModel) -> Unit): MovieViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false)
            val binding = ItemMovieBinding.bind(view)
            return MovieViewHolder(binding, onItemClick)
        }
    }
}
