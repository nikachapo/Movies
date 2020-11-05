package com.example.movies.data

import androidx.paging.PagingData
import com.example.movies.model.MovieModel
import com.example.movies.model.ReviewModel
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {

    fun getPopularMovies(): Flow<PagingData<MovieModel>>

    fun getReviews(movieId: String): Flow<PagingData<ReviewModel>>

    fun searchMovies(query: String): Flow<PagingData<MovieModel>>

    fun getSimilarMovies(movieId: String): Flow<PagingData<MovieModel>>

}