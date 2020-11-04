package com.example.movies.ui.popular_tv_shows

import androidx.paging.PagingData
import com.example.movies.data.MovieRepository
import com.example.movies.model.MovieModel
import com.example.movies.ui.movies_list.MoviesViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PopularTVShowsViewModel @Inject constructor(private val movieRepository: MovieRepository) :
    MoviesViewModel() {

    fun getPopularMovies(refreshData: Boolean = false): Flow<PagingData<MovieModel>> {
        return if (currentResult != null && !refreshData) {
            currentResult!!
        } else {
            setCurrentResult(movieRepository.getPopularMovies())
        }
    }

}