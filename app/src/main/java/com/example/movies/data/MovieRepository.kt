package com.example.movies.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movies.api.MoviesService
import com.example.movies.model.MovieModel
import com.example.movies.paging.MoviesPagingSource
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val moviesService: MoviesService) {

    fun getMovies(query: String): Flow<PagingData<MovieModel>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { MoviesPagingSource(moviesService, query) }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 10
    }

}