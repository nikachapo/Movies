package com.example.movies.paging.source.movies_response

import android.util.Log
import com.example.movies.api.MoviesService
import com.example.movies.model.MovieResponseModel

class SimilarMoviesResponseSource(
    private val moviesService: MoviesService,
    private val movieId: String
) : MoviesResponseSource {

    override suspend fun getResponse(page: Int): MovieResponseModel {
        return moviesService.getSimilarTvShows(movieId, page).also {
            Log.i("taaaaaaaag", "getResponse: ${it.movies}")
        }
    }

}