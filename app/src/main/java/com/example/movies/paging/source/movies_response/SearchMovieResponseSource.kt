package com.example.movies.paging.source.movies_response

import com.example.movies.model.MovieResponseModel
import com.example.movies.api.MoviesService

class SearchMovieResponseSource(
    private val moviesService: MoviesService,
    private val query: String
) : MoviesResponseSource {

    override suspend fun getResponse(page: Int): MovieResponseModel {
        return moviesService.searchRVShow(query, page)
    }

}