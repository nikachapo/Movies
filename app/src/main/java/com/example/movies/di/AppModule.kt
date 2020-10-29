package com.example.movies.di

import android.content.Context
import com.example.movies.db.MainDB
import com.example.movies.db.MoviesDao
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

}