package com.example.movies.di

import com.example.movies.api.MoviesService
import com.example.movies.api.MoviesService.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun bindMoviesService(): MoviesService {
        return provideRetrofit()
            .create(MoviesService::class.java)
    }
}