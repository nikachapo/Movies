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
import com.example.movies.ui.movies_list.LayoutManager
import com.example.movies.ui.movies_list.MovieListPresenterBaseFragment
import com.example.movies.ui.movies_list.Orientation
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class PopularTVShowsFragment : MovieListPresenterBaseFragment(LayoutManager.LINEAR) {

    private lateinit var changeLayoutIV: ImageView

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: PopularTVShowsViewModel
    private var getPopularMoviesJob: Job? = null

    override val containerId = R.id.popularTVShowsListContainer

    override fun showData() = getPopularMovies()
    override fun setLayoutManagerAndOrientation() {
        viewModel.setLayoutManagerAndOrientation()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.popular_tv_shows_fragment, container, false)
        changeLayoutIV = view.findViewById(R.id.changeListShowTypeIV)
        changeLayoutIV.setOnClickListener {
            viewModel.setLayoutManagerAndOrientation()
        }
        if (savedInstanceState == null) {
            changeLayoutIV.setBackgroundResource(getDrawableIdForIV(layoutManager))
            movieListFragment?.changeLayoutManager(layoutManager)
            viewModel.setLayoutManagerAndOrientation(layoutManager, Orientation.VERTICAL)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.layoutManagerAndOrientation.observe(viewLifecycleOwner) {
            changeLayoutIV.setBackgroundResource(getDrawableIdForIV(it.first))
            movieListFragment?.changeLayoutManager(it.first, it.second)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(PopularTVShowsViewModel::class.java)
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
        fun newInstance() = PopularTVShowsFragment()
    }
}