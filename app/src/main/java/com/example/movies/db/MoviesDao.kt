package com.example.movies.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.model.MovieModel

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovies(movies: List<MovieModel>)

    @Query("SELECT * FROM movies_table ORDER BY nextPage")
    fun getMovies(): PagingSource<Int, MovieModel>

    @Query("DELETE FROM movies_table")
    suspend fun clear()

}