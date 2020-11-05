package com.example.movies.paging.source

import com.example.movies.model.MovieModel
import com.example.movies.paging.source.movies_response.MoviesResponseSource

class MoviePagingSource(private val responseSource: MoviesResponseSource) :
    BasePagingSource<Int, MovieModel>() {

    override val startingKey = STARTING_PAGE_INDEX

    override suspend fun getData(key: Int) = responseSource.getResponse(key).movies

    override fun getPreviousKey(currentKey: Int) = currentKey - 1

    override fun getNextKey(currentKey: Int) = currentKey + 1

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

}