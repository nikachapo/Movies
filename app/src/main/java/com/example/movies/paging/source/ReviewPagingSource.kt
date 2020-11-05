package com.example.movies.paging.source

import androidx.paging.PagingSource
import com.example.movies.api.MoviesService
import com.example.movies.model.ReviewModel
import retrofit2.HttpException
import java.io.IOException

class ReviewPagingSource(
    private val moviesService: MoviesService,
    private val id: String
) :
    PagingSource<Int, ReviewModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReviewModel> {

        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val reviews = moviesService.getReviews(id, position).results
            LoadResult.Page(
                data = reviews,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (reviews.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }

    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

}