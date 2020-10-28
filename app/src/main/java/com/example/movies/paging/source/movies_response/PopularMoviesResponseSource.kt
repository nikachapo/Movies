package com.example.movies.paging.source.movies_response

import com.example.movies.api.MovieResponseModel
import com.example.movies.api.MoviesService
import com.example.movies.paging.source.movies_response.MoviesResponseSource

class PopularMoviesResponseSource(private val service: MoviesService) :
    MoviesResponseSource {
    override suspend fun getResponse(page: Int): MovieResponseModel {
        return service.getPopularTVShows(page)
    }

}