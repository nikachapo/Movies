package com.example.movies.paging.source.movies_response

import com.example.movies.api.MovieResponseModel

interface MoviesResponseSource {
    suspend fun getResponse(page: Int): MovieResponseModel
}