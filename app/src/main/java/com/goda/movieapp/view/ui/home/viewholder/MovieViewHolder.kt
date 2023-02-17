package com.goda.movieapp.view.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.goda.movieapp.R
import com.goda.movieapp.domain.pojo.MovieResult
import com.goda.movieapp.view.ui.home.adapter.MovieInteractionListener
import com.goda.movieapp.util.Constants.Companion.BASE_IMAGE_URL_API
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_view_layout.view.*

class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(
        movieResult: MovieResult?,
        listener: MovieInteractionListener
    ) {
        if (movieResult != null) {
            itemView.tv_movie_title.text = movieResult.title
            var url = BASE_IMAGE_URL_API
            url += if (!movieResult.backdrop_path.isNullOrEmpty()) {
                movieResult.backdrop_path
            } else movieResult.poster_path
            if (movieResult.title.length > 30)
                itemView.tv_movie_title.isSelected = true
            itemView.tvVoteAverage.text = movieResult.vote_average.toString()
            Picasso.get().load(url)
                .error(R.drawable.ic_broken_image)
                .placeholder(R.drawable.loading_animation)
                .into(itemView.iv_movie)
            itemView.setOnClickListener { listener.onMovieClick(movieResult, adapterPosition) }
        }
    }

}