package com.example.movies.paging.source

import com.example.movies.api.MoviesService
import com.example.movies.model.ReviewModel

class ReviewPagingSource(
    private val moviesService: MoviesService,
    private val id: String
) : BasePagingSource<Int, ReviewModel>() {

    override val startingKey = STARTING_PAGE_INDEX

    override suspend fun getData(key: Int) = moviesService.getReviews(id, key).results

    override fun getPreviousKey(currentKey: Int) = currentKey - 1

    override fun getNextKey(currentKey: Int) = currentKey + 1

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

}