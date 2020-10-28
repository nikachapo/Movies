package com.example.movies

import android.app.Application
import com.example.movies.di.DaggerAppComponent

class App : Application() {

    val appComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

}