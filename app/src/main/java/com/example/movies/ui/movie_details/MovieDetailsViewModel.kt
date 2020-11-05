package com.example.movies.ui.movie_details

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.data.MovieRepository
import com.example.movies.model.MovieModel
import com.example.movies.model.ReviewModel
import com.example.movies.ui.movies_list.MoviesViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(private val moviesRepository: MovieRepository) :
    MoviesViewModel() {

    private var currentMovieId: String? = null

    private var reviews: Flow<PagingData<ReviewModel>>? = null

    fun getSimilarMovies(movieId: String): Flow<PagingData<MovieModel>> {
        return if (movieId == currentMovieId && currentResult != null) {
            return currentResult!!
        } else {
            currentMovieId = movieId
            setCurrentResult(moviesRepository.getSimilarMovies(movieId))
        }
    }

    fun getReviews(movieId: String): Flow<PagingData<ReviewModel>> {
        return if (movieId == currentMovieId && reviews != null) {
            return reviews!!
        } else {
            currentMovieId = movieId
            val newResult: Flow<PagingData<ReviewModel>> =
                moviesRepository.getReviews(movieId).cachedIn(viewModelScope)
            reviews = newResult
            reviews!!
        }
    }

}