package com.example.movies.di

import android.content.Context
import com.example.movies.di.view_model.ViewModelModule
import com.example.movies.ui.MainActivity
import com.example.movies.ui.PopularTVShowsFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, ViewModelModule::class, NetworkModule::class]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(mainActivity: MainActivity)
    fun inject(popularTVShowsFragment: PopularTVShowsFragment)
}