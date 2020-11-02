package com.example.movies.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.movies.App
import com.example.movies.R
import com.example.movies.ui.movies_list.LayoutManager
import com.example.movies.ui.movies_list.MoviesListFragment
import com.example.movies.ui.movies_list.Orientation
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class PopularTVShowsFragment : Fragment() {

    companion object {
        fun newInstance() = PopularTVShowsFragment()
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: PopularTVShowsViewModel

    private var movieListFragment: MoviesListFragment? = null

    private var getPopularMoviesJob: Job? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.popular_tv_shows_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpFragmentList(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(PopularTVShowsViewModel::class.java)
        getPopularMovies()
    }

    private fun getPopularMovies() {
        getPopularMoviesJob?.cancel()
        getPopularMoviesJob = lifecycleScope.launch {
            viewModel.getPopularMovies().collect { movies ->
                movieListFragment?.submitData(movies)
            }
        }
    }

    private fun setUpFragmentList(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            movieListFragment = MoviesListFragment.newInstance(LayoutManager.GRID)
            childFragmentManager.beginTransaction()
                .add(
                    R.id.popularTVShowsListContainer, movieListFragment!!
                ).commit()
        } else {
            movieListFragment =
                childFragmentManager.findFragmentById(R.id.popularTVShowsListContainer) as MoviesListFragment?
        }
    }
}