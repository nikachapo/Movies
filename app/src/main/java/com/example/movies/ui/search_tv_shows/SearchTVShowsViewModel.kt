package com.example.movies.ui.search_tv_shows

import androidx.paging.PagingData
import com.example.movies.data.IMovieRepository
import com.example.movies.model.MovieModel
import com.example.movies.ui.movies_list.MoviesViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchTVShowsViewModel @Inject constructor(private val movieRepository: IMovieRepository) :
    MoviesViewModel() {

    private var currentQueryValue: String? = null

    fun searchMovie(queryString: String): Flow<PagingData<MovieModel>> {
        return if (queryString == currentQueryValue && currentResult != null) {
            return currentResult!!
        } else {
            currentQueryValue = queryString
            setCurrentResult(movieRepository.searchMovies(queryString))
        }
    }
}