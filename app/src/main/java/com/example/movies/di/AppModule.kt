package com.example.movies.di

import android.content.Context
import com.example.movies.api.MoviesService
import com.example.movies.data.IMovieRepository
import com.example.movies.data.MovieRepositoryImpl
import com.example.movies.data.MoviesRemoteMediator
import com.example.movies.db.MainDB
import com.example.movies.db.MoviesDao
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideDB(context: Context): MainDB {
        return MainDB.getInstance(context)
    }

    @Provides
    fun provideMoviesDao(mainDb: MainDB): MoviesDao {
        return mainDb.moviesDao
    }

    @Provides
    fun provideMovieRepo(
        moviesService: MoviesService,
        remoteMediator: MoviesRemoteMediator,
        moviesDao: MoviesDao
    ): IMovieRepository {
        return MovieRepositoryImpl(moviesService, remoteMediator, moviesDao)
    }
}