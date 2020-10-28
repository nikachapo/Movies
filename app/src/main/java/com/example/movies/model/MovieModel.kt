package com.example.movies.model

import com.google.gson.annotations.SerializedName

data class MovieModel(
    val id: Int,
    val name: String,
    val overview: String,
    @SerializedName("backdrop_path") val backgroundPath: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("first_air_date") val firstAirDate: String,
    @SerializedName("genre_ids") val genreIds: List<Int>,
    @SerializedName("origin_country") val originCountry: List<String>,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int
) {

    val posterUrl: String
        get() = BASE_IMG_PATH + posterPath


    companion object {
        private const val BASE_IMG_PATH = "https://image.tmdb.org/t/p/w200"
    }
}