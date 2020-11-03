package com.example.movies.ui.popular_tv_shows

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.movies.R
import com.example.movies.ui.MainActivity
import com.example.movies.ui.movies_list.LayoutManager
import com.example.movies.ui.movies_list.MoviesListFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class PopularTVShowsFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: PopularTVShowsViewModel
    private var movieListFragment: MoviesListFragment? = null
    private var getPopularMoviesJob: Job? = null
    private var currentManager: LayoutManager = LayoutManager.GRID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.popular_tv_shows_fragment, container, false)

        val changeLayoutIV = view.findViewById<ImageView>(R.id.changeListShowTypeIV)
        changeLayoutIV.run {
            setBackgroundResource(getDrawableIdForIV(currentManager))
            setOnClickListener {
                currentManager = if (currentManager == LayoutManager.GRID) {
                    LayoutManager.LINEAR
                } else {
                    LayoutManager.GRID
                }
                setBackgroundResource(getDrawableIdForIV(currentManager))
                movieListFragment?.changeLayoutManager(currentManager)
            }
        }
        return view
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

    private fun getDrawableIdForIV(layoutManager: LayoutManager): Int {
        return when (layoutManager) {
            LayoutManager.GRID -> R.drawable.ic_grid
            LayoutManager.LINEAR -> R.drawable.ic_linear
        }
    }

    companion object {
        fun newInstance() =
            PopularTVShowsFragment()
    }
}