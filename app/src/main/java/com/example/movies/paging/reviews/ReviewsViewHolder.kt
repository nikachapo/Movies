package com.example.movies.paging.reviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.databinding.ActivityMovieDetailsBinding
import com.example.movies.databinding.ItemReviewLayoutBinding
import com.example.movies.model.ReviewModel

class ReviewsViewHolder(
    private val binding: ItemReviewLayoutBinding
) : RecyclerView.ViewHolder(binding.root) {

    private var review: ReviewModel? = null

    fun bind(review: ReviewModel) {
        showData(review)
    }

    private fun showData(review: ReviewModel) {
        this.review = review
        binding.authorTV.text = review.author
        binding.reviewTV.text = review.content
    }

    companion object {
        fun create(parent: ViewGroup): ReviewsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_review_layout, parent, false)
            val binding =
                ItemReviewLayoutBinding.bind(view)
            return ReviewsViewHolder(binding)
        }
    }
}