package com.example.movies.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movies.model.MovieModel
import com.example.movies.account.Account

@Database(entities = [MovieModel::class, Account::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MainDB : RoomDatabase() {

    abstract val moviesDao: MoviesDao
    abstract val accountDao: AccountDao

    companion object {
        @Volatile
        private var INSTANCE: MainDB? = null

        fun getInstance(context: Context): MainDB =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, MainDB::class.java, "main.db")
                .build()
    }
}