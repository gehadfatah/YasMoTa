package com.goda.movieapp.view.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.View.VISIBLE
import com.goda.movieapp.domain.pagination.PaginationState
import com.goda.movieapp.view.ui.home.adapter.MovieInteractionListener
import kotlinx.android.synthetic.main.loading_view_layout.view.*

class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(status: PaginationState?, listener: MovieInteractionListener) {
        hideViews()
        setVisibleRightViews(status)
        itemView.btn_retry.setOnClickListener{ listener.onClickRetry() }
    }

    private fun hideViews() {
        itemView.tv_error_message.visibility = View.GONE
        itemView.btn_retry.visibility = View.GONE
    }

    private fun setVisibleRightViews(paginationState: PaginationState?) {
        when (paginationState) {
            PaginationState.ERROR -> {
                itemView.btn_retry.visibility = VISIBLE
                itemView.tv_error_message.visibility = VISIBLE
            }
            else -> {}
        }
    }

}