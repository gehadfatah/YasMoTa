package com.goda.movieapp.view.ui.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.goda.movieapp.R
import com.goda.movieapp.domain.pojo.MovieResult
import com.goda.movieapp.view.ui.home.adapter.MovieInteractionListener
import com.goda.movieapp.view.ui.home.viewholder.MovieViewHolder

class FavoriteListAdapter(private val listener: MovieInteractionListener) : RecyclerView.Adapter<MovieViewHolder>() {

    private var movies: List<MovieResult> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.movie_view_layout, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie, listener)
    }

    fun submitList(submitMovies: List<MovieResult>) {
        this.movies = submitMovies
        notifyDataSetChanged()
    }
}