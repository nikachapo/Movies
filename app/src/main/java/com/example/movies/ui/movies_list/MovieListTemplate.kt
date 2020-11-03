package com.example.movies.ui.movies_list

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.paging.PagingData
import com.example.movies.databinding.FragmentMoviesListBinding
import com.example.movies.model.MovieModel
import com.example.movies.paging.MoviesAdapter

interface MovieListTemplate : LifecycleObserver {

    val binding: FragmentMoviesListBinding
    val mView: View

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStartEventHandler() {
        setUpViews()
        initAdapter()
    }

    fun initAdapter()

    fun setUpViews()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun handleArguments()

    suspend fun submitData(data: PagingData<MovieModel>)
    fun changeLayoutManager(
        currentManager: LayoutManager,
        orientation: Orientation = Orientation.VERTICAL
    )
}