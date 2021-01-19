package com.example.movies.di.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movies.ui.login.LoginViewModel
import com.example.movies.ui.movie_details.MovieDetailsViewModel
import com.example.movies.ui.popular_tv_shows.PopularTVShowsViewModel
import com.example.movies.ui.profile.AccountProfileViewModel
import com.example.movies.ui.registration.choose_picture.PictureChooseViewModel
import com.example.movies.ui.registration.enter_details.EnterDetailsViewModel
import com.example.movies.ui.search_tv_shows.SearchTVShowsViewModel
import com.example.movies.ui.registration.RegistrationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    abstract fun bindMyViewModel(registrationViewModel: RegistrationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EnterDetailsViewModel::class)
    abstract fun bindEnterDetailsViewModel(enterDetailsViewModel: EnterDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindEnterLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PictureChooseViewModel::class)
    abstract fun bindPictureChooseViewModel(loginViewModel: PictureChooseViewModel): ViewModel

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
    @IntoMap
    @ViewModelKey(AccountProfileViewModel::class)
    abstract fun bindAccDetailsViewModel(accountProfileViewModel: AccountProfileViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}