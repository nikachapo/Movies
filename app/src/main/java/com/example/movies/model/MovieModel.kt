package com.example.movies.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "movies_table")
@TypeConverters()
data class MovieModel(
    @PrimaryKey
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("backdrop_path") val backgroundPath: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("genre_ids") val genreIds: List<Long>?,
    @SerializedName("original_language") val originalLanguage: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
    @SerializedName("vote_count") val voteCount: Int?
) : Serializable {

    @SerializedName("page")
    var page: Int = 0

    @SerializedName("next_page")
    var nextPage: Int? = null

    val posterUrl: String
        get() = BASE_IMG_PATH + posterPath

    val backgroundUrl: String
        get() = BASE_IMG_PATH + backgroundPath
}

private const val BASE_IMG_PATH = "https://image.tmdb.org/t/p/w200"