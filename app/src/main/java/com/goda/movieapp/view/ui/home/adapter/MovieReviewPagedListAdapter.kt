package com.goda.movieapp.view.ui.home.adapter

import android.view.LayoutInflater
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.goda.movieapp.R
import com.goda.movieapp.domain.pagination.PaginationState
import com.goda.movieapp.domain.pojo.ResultReviews
import com.goda.movieapp.view.ui.home.viewholder.LoadingReviewViewHolder
import com.goda.movieapp.view.ui.home.viewholder.MovieReviewViewHolder

class MovieReviewPagedListAdapter(private val listener: OnRetryReview) :
    PagedListAdapter<ResultReviews, RecyclerView.ViewHolder>(diffUtilCallback) {

    private var state: PaginationState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_recycleview_reviews -> MovieReviewViewHolder(view)
            R.layout.loading_view_layout -> LoadingReviewViewHolder(view)
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_recycleview_reviews -> (holder as MovieReviewViewHolder).bind(
                getItem(position)

            )
            R.layout.loading_view_layout -> (holder as LoadingReviewViewHolder).bind(state, listener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) {
            R.layout.item_recycleview_reviews
        } else {
            R.layout.loading_view_layout
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == PaginationState.LOADING || state == PaginationState.ERROR)
    }

    fun updatePaginationState(newState: PaginationState) {
        this.state = newState
        if(newState != PaginationState.LOADING){
            notifyDataSetChanged()
        }
    }

    companion object {
        private val diffUtilCallback = object : DiffUtil.ItemCallback<ResultReviews>() {
            override fun areItemsTheSame(oldItem: ResultReviews, newItem: ResultReviews): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ResultReviews, newItem: ResultReviews): Boolean {
                return oldItem == newItem
            }
        }
    }


}