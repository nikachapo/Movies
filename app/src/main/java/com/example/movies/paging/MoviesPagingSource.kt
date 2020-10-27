package com.example.movies.paging

import androidx.paging.PagingSource
import com.example.movies.api.MoviesService
import com.example.movies.model.MovieModel
import retrofit2.HttpException
import java.io.IOException

private const val GITHUB_STARTING_PAGE_INDEX = 1

class MoviesPagingSource(
    private val service: MoviesService,
    private val query: String
) : PagingSource<Int, MovieModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {
        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX
        val apiQuery = query
        return try {
            val response = service.searchMovie(apiQuery, position)
            val movies = response.movies
            LoadResult.Page(
                data = movies,
                prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (movies.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}