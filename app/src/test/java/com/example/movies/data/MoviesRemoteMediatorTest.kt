package com.example.movies.data

import androidx.paging.*
import com.example.movies.BaseCoroutinesTest
import com.example.movies.api.MoviesService
import com.example.movies.db.MoviesDao
import com.example.movies.model.MovieModel
import com.example.movies.model.MovieResponseModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.io.IOException

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class MoviesRemoteMediatorTest : BaseCoroutinesTest() {

    private lateinit var moviesRemoteMediator: MoviesRemoteMediator

    private lateinit var mockService: MoviesService
    private lateinit var mockDao: MoviesDao
    private val testResponse = MovieResponseModel(
        1, listOf(
            MovieModel(id = 1, name = "movie1", null, null, null, null, null, null, null, null),
            MovieModel(id = 2, name = "movie2", null, null, null, null, null, null, null, null),
            MovieModel(id = 3, name = "movie3", null, null, null, null, null, null, null, null)
        ), 1
    )

    @Before
    fun setUp() {
        mockService = mock(MoviesService::class.java)
        mockDao = mock(MoviesDao::class.java)
        moviesRemoteMediator = MoviesRemoteMediator(mockService, mockDao)
    }

    @Test
    fun `load when service is available`() {
        mainCoroutineRule.runBlockingTest {
            `when`(mockService.getPopularTVShows(1)).thenReturn(testResponse)
            val result = moviesRemoteMediator.load(
                LoadType.REFRESH,
                PagingState(
                    listOf(PagingSource.LoadResult.Page(testResponse.movies, null, null)),
                    1,
                    PagingConfig(pageSize = 1),
                    1
                )
            )
            verify(mockService).getPopularTVShows(1)
            verify(mockDao).saveMovies(testResponse.movies)
            assertThat(result, instanceOf(RemoteMediator.MediatorResult.Success::class.java))
        }
    }

    @Test
    fun `load when service is unavailable`() {
        mainCoroutineRule.runBlockingTest {
            `when`(mockService.getPopularTVShows(1)).thenAnswer { throw IOException() }
            val result = moviesRemoteMediator.load(
                LoadType.REFRESH,
                PagingState(
                    listOf(PagingSource.LoadResult.Page(testResponse.movies, null, null)),
                    1,
                    PagingConfig(pageSize = 1),
                    1
                )
            )
            verify(mockService).getPopularTVShows(1)
            verify(mockDao, times(0)).saveMovies(testResponse.movies)
            assertThat(result, instanceOf(RemoteMediator.MediatorResult.Error::class.java))
        }
    }

    @Test
    fun `MoviesDao_clear is called when LoadType is REFRESH`() {
        mainCoroutineRule.runBlockingTest {
            `when`(mockService.getPopularTVShows(1)).thenReturn(testResponse)
            moviesRemoteMediator.load(
                LoadType.REFRESH,
                PagingState(
                    listOf(PagingSource.LoadResult.Page(testResponse.movies, null, null)),
                    1,
                    PagingConfig(pageSize = 1),
                    1
                )
            )
            verify(mockDao).clear()
        }
    }
}