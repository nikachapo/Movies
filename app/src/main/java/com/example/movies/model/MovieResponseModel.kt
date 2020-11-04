package com.example.movies.model

import com.google.gson.annotations.SerializedName

class MovieResponseModel(
    @SerializedName("page") var page: Int,
    _movies: List<MovieModel>,
    @SerializedName("total_pages") val totalPages: Int
) {
    @SerializedName("results")
    val movies: List<MovieModel> = _movies
        get() {
            field.forEach { movie ->
                movie.page = this.page + 1
                movie.nextPage = if (this.page <= totalPages) this.page + 1 else null
            }
            return field
        }

    val nextPage: Int?
        get() {
            return if (page <= totalPages) page + 1 else null
        }
}