package com.example.movies.ui.popular_tv_shows

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.movies.R
import com.example.movies.ui.MainActivity
import com.example.movies.ui.MovieListPresenterBaseFragment
import com.example.movies.ui.movies_list.LayoutManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class PopularTVShowsFragment : MovieListPresenterBaseFragment(LayoutManager.GRID) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: PopularTVShowsViewModel
    private var getPopularMoviesJob: Job? = null

    override val containerId: Int
        get() = R.id.popularTVShowsListContainer

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
            setOnClickListener { changeLayoutManager() }
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(PopularTVShowsViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
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

    companion object {
        fun newInstance() =
            PopularTVShowsFragment()
    }
}