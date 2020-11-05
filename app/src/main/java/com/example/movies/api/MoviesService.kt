package com.example.movies.api

import com.example.movies.model.GenresResponseModel
import com.example.movies.model.MovieResponseModel
import com.example.movies.model.ReviewResponseModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MoviesService {

    @GET("tv/popular")
    suspend fun getPopularTVShows(
        @Query("page") page: Int? = null,
        @Query("api_key") key: String = "b96423827404b3c13297beb0e141ba86"
    ): MovieResponseModel

    @GET("search/tv")
    suspend fun searchRVShow(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("api_key") key: String = "b96423827404b3c13297beb0e141ba86"
    ): MovieResponseModel

    @GET("tv/{tv_id}/similar")
    suspend fun getSimilarTvShows(
        @Path("tv_id") tvId: String,
        @Query("page") page: Int,
        @Query("api_key") key: String = "b96423827404b3c13297beb0e141ba86"
    ): MovieResponseModel

    @GET("genre/tv/list")
    suspend fun getGenresResponse(
        @Query("api_key") key: String = "b96423827404b3c13297beb0e141ba86"
    ): GenresResponseModel

    @GET("tv/{tv_id}/reviews")
    suspend fun getReviews(
        @Path("tv_id") tvId: String,
        @Query("page") page: Int,
        @Query("api_key") key: String = "b96423827404b3c13297beb0e141ba86"
    ): ReviewResponseModel

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }

}
