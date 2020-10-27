package com.example.movies.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api.themoviedb.org/"

interface MoviesService {

    @GET("3/tv/popular")
    suspend fun getMovies(
        @Query("page") page: String = "1",
        @Query("api_key") key: String = "b96423827404b3c13297beb0e141ba86"
    ): MovieResponseModel

    companion object {

        fun create(): MoviesService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MoviesService::class.java)
        }
    }

}
