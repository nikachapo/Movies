package com.example.movies.paging.source.movies_response

import com.example.movies.api.MovieResponseModel
import com.example.movies.api.MoviesService
import com.example.movies.paging.source.movies_response.MoviesResponseSource

class SearchMovieResponseSource(
    private val moviesService: MoviesService,
    private val query: String
) : MoviesResponseSource {

    override suspend fun getResponse(page: Int): MovieResponseModel {
        return moviesService.searchRVShow(query, page)
    }

}