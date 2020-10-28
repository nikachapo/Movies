package com.example.movies.paging.source

import androidx.paging.PagingSource
import com.example.movies.model.MovieModel
import com.example.movies.paging.source.movies_response.MoviesResponseSource
import retrofit2.HttpException
import java.io.IOException

class MoviePagingSource(private val responseSource: MoviesResponseSource) :
    PagingSource<Int, MovieModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {

        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val movies = responseSource.getResponse(position).movies
            LoadResult.Page(
                data = movies,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (movies.isEmpty()) null else position + 1
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