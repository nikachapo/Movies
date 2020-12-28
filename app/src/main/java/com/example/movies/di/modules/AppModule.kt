package com.example.movies.di.modules

import android.content.Context
import com.example.movies.api.MoviesService
import com.example.movies.data.IMovieRepository
import com.example.movies.data.MovieRepositoryImpl
import com.example.movies.data.MoviesRemoteMediator
import com.example.movies.db.*
import com.example.movies.network.AccountRemoteService
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
    fun provideAccountDao(mainDb: MainDB): AccountDao {
        return mainDb.accountDao
    }

    @Provides
    fun provideAccountRepo(accountRemoteService: AccountRemoteService, accountDao: AccountDao): AccountRepository {
        return AccountRepositoryImpl(accountRemoteService, accountDao)
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