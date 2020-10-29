package com.example.movies.api

import retrofit2.http.GET
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

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }

}
