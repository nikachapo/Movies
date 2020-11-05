package com.example.movies.model

import com.google.gson.annotations.SerializedName

data class ReviewModel(
    @SerializedName("author") val author: String,
    @SerializedName("content") val content: String,
    @SerializedName("id") val id: String,
    @SerializedName("url") val url: String
)