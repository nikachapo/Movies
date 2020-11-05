package com.example.movies.model

import com.google.gson.annotations.SerializedName

data class ReviewResponseModel(
    @SerializedName("id") val id: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<ReviewModel>,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("total_results") val total_results: Int
)