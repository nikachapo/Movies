package com.example.movies.model

data class ReviewResponseModel(
    val id: Int,
    val page: Int,
    val results: List<ReviewModel>,
    val total_pages: Int,
    val total_results: Int
)