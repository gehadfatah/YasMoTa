package com.goda.movieapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.goda.movieapp.R;
import com.goda.movieapp.domain.pojo.MovieResult;

import java.util.List;

import static com.goda.movieapp.util.Constants.BASE_IMAGE_URL_API;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context mContext;
    private List<MovieResult> movieList;
    private MovieResult currentMovie;
    private SearchAdapterOnClickHandler clickHandler;

    public interface SearchAdapterOnClickHandler {
        void onClick(MovieResult movie);
    }

    public SearchAdapter(Context mContext, List<MovieResult> movieList, SearchAdapterOnClickHandler clickHandler) {
        this.mContext = mContext;
        this.movieList = movieList;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_view_layout, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        currentMovie = movieList.get(position);
        holder.movieTitle.setText(currentMovie.getTitle());
        holder.movieRating.setText((int) currentMovie.getVote_average());
        Glide.with(mContext)
                .load(BASE_IMAGE_URL_API + currentMovie.getBackdrop_path())
                .into(holder.moviePoster);


    }

    @Override
    public int getItemCount() {
        if (movieList == null) {
            return 0;
        }
        return movieList.size();
    }

    public void clear() {
        int size = movieList.size();
        movieList.clear();
        notifyItemRangeRemoved(0, size);
    }

    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView movieTitle;
        TextView movieRating;
        ImageView moviePoster;

        private SearchViewHolder(View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.tv_movie_title);
            movieRating = itemView.findViewById(R.id.tvVoteAverage);
            moviePoster = itemView.findViewById(R.id.iv_movie);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            currentMovie = movieList.get(position);
            clickHandler.onClick(currentMovie);
        }
    }


}
