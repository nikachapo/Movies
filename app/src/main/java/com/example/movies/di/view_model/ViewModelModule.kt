package com.example.movies.di.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movies.ui.movie_details.MovieDetailsViewModel
import com.example.movies.ui.popular_tv_shows.PopularTVShowsViewModel
import com.example.movies.ui.search_tv_shows.SearchTVShowsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PopularTVShowsViewModel::class)
    abstract fun bindPopularTVShowsViewModel(popularTVShowsViewModel: PopularTVShowsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchTVShowsViewModel::class)
    abstract fun bindSearchTVShowsViewModel(searchTVShowsViewModel: SearchTVShowsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    abstract fun bindMovieDetViewModel(movieDetailsViewModel: MovieDetailsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}