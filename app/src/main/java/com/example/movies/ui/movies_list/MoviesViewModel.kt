package com.example.movies.ui.movies_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.model.MovieModel
import kotlinx.coroutines.flow.Flow

abstract class MoviesViewModel : ViewModel() {

    private var _layoutManagerAndOrientation = MutableLiveData<Pair<LayoutManager, Orientation>>()

    val layoutManagerAndOrientation: LiveData<Pair<LayoutManager, Orientation>> =
        _layoutManagerAndOrientation

    fun setLayoutManagerAndOrientation(
        layoutManager: LayoutManager? = null,
        orientation: Orientation? = null
    ) {
        val currentLManager = layoutManager ?: _layoutManagerAndOrientation.value?.first
        val lManager = if (currentLManager == LayoutManager.LINEAR) {
            LayoutManager.GRID
        } else {
            LayoutManager.LINEAR
        }

        val currentOrientation = orientation ?: _layoutManagerAndOrientation.value?.second
        val lOrientation = if (currentOrientation == Orientation.VERTICAL) {
            Orientation.HORIZONTAL
        } else {
            Orientation.VERTICAL
        }
        _layoutManagerAndOrientation.value = Pair(lManager, lOrientation)
    }

    protected var currentResult: Flow<PagingData<MovieModel>>? = null

    protected fun setCurrentResult(
        movies: Flow<PagingData<MovieModel>>
    ): Flow<PagingData<MovieModel>> {

        val newResult: Flow<PagingData<MovieModel>> = movies
            .cachedIn(viewModelScope)
        currentResult = newResult
        return newResult

    }
}