package com.example.movies.paging.reviews

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.databinding.ActivityMovieDetailsBinding
import com.example.movies.model.ReviewModel

class ReviewsAdapter : PagingDataAdapter<ReviewModel, RecyclerView.ViewHolder>(REVIEW_COMPARATOR) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return ReviewsViewHolder.create(parent)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val review = getItem(position)
        if (review != null) {
            (holder as ReviewsViewHolder).bind(review)
        }
    }

    companion object {
        private val REVIEW_COMPARATOR = object : DiffUtil.ItemCallback<ReviewModel>() {
            override fun areItemsTheSame(oldItem: ReviewModel, newItem: ReviewModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ReviewModel, newItem: ReviewModel): Boolean {
                return oldItem == newItem
            }

        }
    }
}
