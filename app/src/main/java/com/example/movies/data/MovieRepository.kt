package com.example.movies.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movies.api.MoviesService
import com.example.movies.model.MovieModel
import com.example.movies.paging.source.MoviePagingSource
import com.example.movies.paging.source.movies_response.PopularMoviesResponseSource
import com.example.movies.paging.source.movies_response.SearchMovieResponseSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepository @Inject constructor(private val moviesService: MoviesService) {

    fun getPopularMovies(): Flow<PagingData<MovieModel>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                MoviePagingSource(PopularMoviesResponseSource(moviesService))
            }
        ).flow
    }

    fun searchMovies(query: String): Flow<PagingData<MovieModel>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                MoviePagingSource(SearchMovieResponseSource(moviesService, query))
            }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 10
    }

}