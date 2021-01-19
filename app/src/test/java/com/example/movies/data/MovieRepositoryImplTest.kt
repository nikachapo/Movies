package com.example.movies.data
//
//import androidx.paging.ExperimentalPagingApi
//import androidx.paging.Pager
//import androidx.paging.PagingData
//import com.example.movies.BaseCoroutinesTest
//import com.example.movies.api.MoviesService
//import com.example.movies.db.MoviesDao
//import com.example.movies.model.MovieModel
//import com.example.movies.model.MovieResponseModel
//import com.example.movies.paging.IPagerFactory
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.test.runBlockingTest
//import org.hamcrest.CoreMatchers.`is`
//import org.hamcrest.MatcherAssert.assertThat
//import org.junit.Before
//import org.junit.Test
//import org.mockito.Mockito.*
//
//@Suppress("UNCHECKED_CAST")
//@ExperimentalCoroutinesApi
//@ExperimentalPagingApi
//class MovieRepositoryImplTest : BaseCoroutinesTest() {
//
//    private lateinit var mockMovieService: MoviesService
//    private lateinit var mockRemoteMediator: MoviesRemoteMediator
//    private lateinit var mockDao: MoviesDao
//    private lateinit var mockPagerFactory: IPagerFactory
//    private lateinit var moviesRepository: IMovieRepository
//
//    private val testResponse = MovieResponseModel(
//        1, listOf(
//            MovieModel(id = 1, name = "movie1"),
//            MovieModel(id = 2, name = "movie2"),
//            MovieModel(id = 3, name = "movie3")
//        ), 1
//    )
//
//    @Before
//    fun setUp() {
//        mockMovieService = mock(MoviesService::class.java)
//        mockDao = mock(MoviesDao::class.java)
//        mockPagerFactory = mock(IPagerFactory::class.java)
//        mockRemoteMediator = MoviesRemoteMediator(mockMovieService, mockDao)
//        moviesRepository =
//            MovieRepositoryImpl(mockMovieService, mockRemoteMediator, mockDao, mockPagerFactory)
//    }
//
//    @Test
//    fun `getPopularMovies - remote service is available`() {
//        mainCoroutineRule.runBlockingTest {
//            val pager = mock(Pager::class.java) as Pager<Int, MovieModel>
//            `when`(pager.flow).thenReturn(flowOf(PagingData.from(testResponse.movies)))
//            `when`(
//                mockPagerFactory.create(
//                    moviesRepository.config,
//                    mockDao.getMovies(),
//                    mockRemoteMediator
//                )
//            ).thenReturn(pager)
//
//            `when`(mockMovieService.getPopularTVShows()).thenReturn(testResponse)
//            val moviesFlow = moviesRepository.getPopularMovies()
//            PagingData.from(listOf())
////            verify(mockDao).saveMovies(testResponse.movies)
////            verify(mockDao).getMovies()
//        }
//    }
//
////    @Test
////    fun searchMovies() {
////    }
////
////    @Test
////    fun getSimilarMovies() {
////    }
////
////    @Test
////    fun getReviews() {
////    }
//}