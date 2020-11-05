package com.example.movies.ui.movies_list

import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.movies.R

abstract class MovieListPresenterBaseFragment(layoutManager: LayoutManager) : Fragment() {

    protected var movieListFragment: MoviesListFragment? = null
    protected var currentManager: LayoutManager = layoutManager

    abstract val containerId: Int

    abstract fun showData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpFragmentList(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        showData()
    }

    private fun setUpFragmentList(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            movieListFragment = MoviesListFragment.newInstance(currentManager)
            childFragmentManager.beginTransaction()
                .add(containerId, movieListFragment!!).commit()
        } else {
            movieListFragment =
                childFragmentManager.findFragmentById(containerId) as MoviesListFragment?
        }
    }

    protected fun ImageView.changeLayoutManager() {
        currentManager = if (currentManager == LayoutManager.GRID) {
            LayoutManager.LINEAR
        } else {
            LayoutManager.GRID
        }
        setBackgroundResource(getDrawableIdForIV(currentManager))
        movieListFragment?.changeLayoutManager(currentManager)
    }

    protected fun getDrawableIdForIV(layoutManager: LayoutManager): Int {
        return when (layoutManager) {
            LayoutManager.GRID -> R.drawable.ic_grid
            LayoutManager.LINEAR -> R.drawable.ic_linear
        }
    }

}