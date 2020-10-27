package com.example.movies.api

import com.example.movies.model.MovieModel
import com.google.gson.annotations.SerializedName

data class MovieResponseModel(
    val page: Int,
    @SerializedName("results") val movies: List<MovieModel>,
    @SerializedName("total_pages") val totalPages: Int
)