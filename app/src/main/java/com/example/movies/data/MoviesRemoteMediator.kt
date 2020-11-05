package com.example.movies.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.movies.api.MoviesService
import com.example.movies.db.MainDB
import com.example.movies.db.MoviesDao
import com.example.movies.model.MovieModel
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator @Inject constructor(
    private val mainDB: MainDB,
    private val service: MoviesService,
    private val moviesDao: MoviesDao
) : RemoteMediator<Int, MovieModel>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieModel>
    ): MediatorResult {
        return try {

            val loadKey = when (loadType) {
                LoadType.REFRESH -> START_PAGE_INDEX
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    lastItem.nextPage
                }
            }

            val response = service.getPopularTVShows(loadKey)

            mainDB.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    moviesDao.clear()
                }
                moviesDao.saveMovies(response.movies)
            }

            MediatorResult.Success(endOfPaginationReached = response.nextPage == null)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    companion object {
        private const val START_PAGE_INDEX = 1
    }
}