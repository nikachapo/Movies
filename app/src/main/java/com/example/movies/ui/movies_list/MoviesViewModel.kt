package com.example.movies.ui.movies_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.model.MovieModel
import kotlinx.coroutines.flow.Flow

abstract class MoviesViewModel : ViewModel() {

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