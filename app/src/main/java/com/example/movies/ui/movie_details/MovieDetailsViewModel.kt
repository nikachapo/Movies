package com.example.movies.ui.movie_details

import androidx.paging.PagingData
import com.example.movies.data.MovieRepository
import com.example.movies.model.MovieModel
import com.example.movies.ui.movies_list.MoviesViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(private val moviesRepository: MovieRepository) :
    MoviesViewModel() {

    private var currentMovieId: String? = null

    fun getSimilarMovies(movieId: String): Flow<PagingData<MovieModel>> {
        return if (movieId == currentMovieId && currentResult != null) {
            return currentResult!!
        } else {
            currentMovieId = movieId
            setCurrentResult(moviesRepository.getSimilarMovies(movieId))
        }
    }

}