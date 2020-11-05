package com.example.movies.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movies.api.MoviesService
import com.example.movies.db.MoviesDao
import com.example.movies.model.MovieModel
import com.example.movies.model.ReviewModel
import com.example.movies.paging.source.MoviePagingSource
import com.example.movies.paging.source.ReviewPagingSource
import com.example.movies.paging.source.movies_response.SearchMovieResponseSource
import com.example.movies.paging.source.movies_response.SimilarMoviesResponseSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val moviesService: MoviesService,
    private val remoteMediator: MoviesRemoteMediator,
    private val moviesDao: MoviesDao
) : IMovieRepository {

    private val config = PagingConfig(pageSize = PAGE_SIZE)

    override fun getPopularMovies(): Flow<PagingData<MovieModel>> {
        return Pager(
            config = config,
            remoteMediator = remoteMediator,
            pagingSourceFactory = { moviesDao.getMovies() }
        ).flow
    }

    override fun searchMovies(query: String): Flow<PagingData<MovieModel>> {
        return Pager(
            config = config,
            pagingSourceFactory = {
                MoviePagingSource(
                    SearchMovieResponseSource(moviesService, query)
                )
            }
        ).flow
    }

    override fun getSimilarMovies(movieId: String): Flow<PagingData<MovieModel>> {
        return Pager(
            config = config,
            pagingSourceFactory = {
                MoviePagingSource(
                    SimilarMoviesResponseSource(moviesService, movieId)
                )
            }
        ).flow
    }

    override fun getReviews(movieId: String): Flow<PagingData<ReviewModel>> {
        return Pager(
            config = config,
            pagingSourceFactory = {
                ReviewPagingSource(moviesService, movieId)
            }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 20
    }

}