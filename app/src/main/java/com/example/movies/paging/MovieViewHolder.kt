/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.movies.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.databinding.ItemMovieBinding
import com.example.movies.model.MovieModel
import com.example.movies.utils.loadImage

class MovieViewHolder(private val binding: ItemMovieBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var movie: MovieModel? = null
    private val view = binding.root

    init {
        view.setOnClickListener {
//            TODO implement details screen
        }
    }

    fun bind(movie: MovieModel) {
        showMovieData(movie)
    }

    private fun showMovieData(movie: MovieModel) {
        this.movie = movie
        binding.itemNameTV.text = movie.name
        binding.itemPosterIV.loadImage(movie.posterUrl)
    }

    companion object {
        fun create(parent: ViewGroup): MovieViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false)
            val binding = ItemMovieBinding.bind(view)
            return MovieViewHolder(binding)
        }
    }
}
