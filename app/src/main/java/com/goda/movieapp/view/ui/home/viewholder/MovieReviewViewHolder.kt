package com.goda.movieapp.view.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.goda.movieapp.domain.pojo.ResultReviews
import kotlinx.android.synthetic.main.item_recycleview_reviews.view.*

class MovieReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(
        review: ResultReviews?
    ) {
        if (review != null) {

            itemView.tvAuthorReview.text = review.author.toUpperCase()
            itemView.tvContentReview.run {
                text = review.content

            }
            itemView.tvUrl.apply {
                text = review.url
            }
            itemView.tvNumber.text = (position + 1).toString()
        }
    }

}