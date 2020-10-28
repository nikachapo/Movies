package com.example.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.data.MovieRepository
import com.example.movies.model.MovieModel
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val repository: MovieRepository) : ViewModel() {

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<MovieModel>>? = null

    fun searchRepo(queryString: String): Flow<PagingData<MovieModel>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<MovieModel>> = repository.getMovies(queryString)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    override fun onCleared() {
        super.onCleared()
    }

}