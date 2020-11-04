package com.example.movies.paging.source.movies_response

import com.example.movies.model.MovieResponseModel

interface MoviesResponseSource {
    suspend fun getResponse(page: Int): MovieResponseModel
}