package com.example.movies.di.modules

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IO

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class Main

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class Default

@Module
class DispatchersModule {

    @Main
    @Provides
    fun provideMain() : CoroutineDispatcher {
        return Dispatchers.Main
    }

    @IO
    @Provides
    fun provideIO() : CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Default
    @Provides
    fun provideDefault() : CoroutineDispatcher {
        return Dispatchers.Default
    }

}