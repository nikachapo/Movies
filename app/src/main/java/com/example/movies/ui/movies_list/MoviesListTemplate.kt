package com.example.movies.ui.movies_list

import android.os.Bundle
import android.view.View
import com.example.movies.databinding.FragmentMoviesListBinding
import com.example.movies.model.MovieModel

interface MoviesListTemplate : PagedListListeners<MovieModel> {

    val binding: FragmentMoviesListBinding
    val mView: View

    fun setUpViews()

    fun handleArguments(arguments: Bundle?)
}