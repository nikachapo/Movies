package com.example.movies

import android.app.Application
import com.example.movies.di.AppComponent
import com.example.movies.di.DaggerAppComponent
import com.example.movies.utils.getDefaultGenresResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class App : Application() {

    @Volatile
    var genresMap = mutableMapOf<Long, String>()

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                appComponent.moviesService().getGenresResponse().genres
                    .forEach { genresMap[it.id] = it.name }
            } catch (e: IOException) {
                getDefaultGenresResponse()?.genres?.forEach { genresMap[it.id] = it.name }
            } catch (e: HttpException) {
                getDefaultGenresResponse()?.genres?.forEach { genresMap[it.id] = it.name }
            }
        }
    }

}