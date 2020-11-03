package com.example.movies.ui.popular_tv_shows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.data.MovieRepository
import com.example.movies.model.MovieModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PopularTVShowsViewModel @Inject constructor(private val movieRepository: MovieRepository) :
    ViewModel() {

    private var currentResult: Flow<PagingData<MovieModel>>? = null

    fun getPopularMovies(refreshData: Boolean = false): Flow<PagingData<MovieModel>> {
        return if (currentResult != null && !refreshData) {
            currentResult!!
        } else {
            val newResult: Flow<PagingData<MovieModel>> = movieRepository.getPopularMovies()
                .cachedIn(viewModelScope)
            currentResult = newResult
            currentResult!!
        }
    }
}