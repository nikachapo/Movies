package com.example.movies.ui.movies_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.movies.R

abstract class MovieListPresenterBaseFragment(protected val layoutManager: LayoutManager) : Fragment() {

    protected var movieListFragment: MoviesListFragment? = null

    abstract val containerId: Int

    abstract fun showData()

    abstract fun setLayoutManagerAndOrientation()

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
            movieListFragment = MoviesListFragment.newInstance(layoutManager)
            childFragmentManager.beginTransaction()
                .add(containerId, movieListFragment!!).commit()
        } else {
            movieListFragment =
                childFragmentManager.findFragmentById(containerId) as MoviesListFragment?
        }
    }

    protected fun getDrawableIdForIV(layoutManager: LayoutManager): Int {
        return when (layoutManager) {
            LayoutManager.GRID -> R.drawable.ic_grid
            LayoutManager.LINEAR -> R.drawable.ic_linear
        }
    }

}