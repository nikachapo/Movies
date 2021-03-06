package com.example.movies.di

import android.content.Context
import com.example.movies.api.MoviesService
import com.example.movies.di.modules.AppModule
import com.example.movies.di.modules.FirebaseModule
import com.example.movies.di.modules.NetworkModule
import com.example.movies.di.modules.StorageModule
import com.example.movies.di.view_model.ViewModelModule
import com.example.movies.ui.MainActivity
import com.example.movies.ui.login.LoginActivity
import com.example.movies.ui.movie_details.MovieDetailsActivity
import com.example.movies.ui.popular_tv_shows.PopularTVShowsFragment
import com.example.movies.ui.profile.AccountProfileFragment
import com.example.movies.ui.search_tv_shows.SearchTVShowsFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class,
        AppSubComponents::class,
        ViewModelModule::class,
        NetworkModule::class,
        StorageModule::class]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance genresMap: MutableMap<Long, String>
        ): AppComponent
    }

    fun moviesService(): MoviesService

    fun registrationComponentFactory(): RegistrationComponent.Factory

    fun inject(loginActivity: LoginActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(popularTVShowsFragment: PopularTVShowsFragment)
    fun inject(searchTVShowsFragment: SearchTVShowsFragment)
    fun inject(movieDetailsActivity: MovieDetailsActivity)
    fun inject(accountProfileFragment: AccountProfileFragment)
}