package com.example.movies.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.data.MovieRepository
import com.example.movies.model.MovieModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<MovieModel>>? = null

    fun searchMovie(queryString: String): Flow<PagingData<MovieModel>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        return setCurrentResult(repository.searchMovies(queryString))
    }

    private fun setCurrentResult(
        movies: Flow<PagingData<MovieModel>>
    ): Flow<PagingData<MovieModel>> {
        val newResult: Flow<PagingData<MovieModel>> = movies
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    fun getPopularMovies(): Flow<PagingData<MovieModel>> {
        return if (currentSearchResult == null || currentQueryValue == null) {
            setCurrentResult(repository.getPopularMovies())
        } else {
            currentSearchResult!!
        }
    }

}