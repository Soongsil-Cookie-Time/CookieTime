package com.ssuclass.cookietime.presentation.search;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.network.response.TMDBMovieSearchResponse;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private final List<TMDBMovieSearchResponse.Movie> movieList;
    private final OnMovieClickListener listener;

    public SearchAdapter(List<TMDBMovieSearchResponse.Movie> movieList, OnMovieClickListener listener) {
        this.movieList = movieList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TMDBMovieSearchResponse.Movie movie = movieList.get(position);
        holder.titleTextView.setText(movie.getTitle());
        holder.releaseDateTextView.setText(movie.getReleaseDate() + " 개봉");
        holder.voteAverageTextView.setText("평점 : " + movie.getVoteAverage());

        String imageUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
        Glide.with(holder.itemView.getContext()).load(imageUrl).into(holder.posterImageView);

        holder.cookieDetailButton.setOnClickListener(v -> listener.onMovieClick(movie.getId(), movie.getTitle()));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView releaseDateTextView;
        private final TextView voteAverageTextView;
        private final ImageView posterImageView;
        private final Button cookieDetailButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cookieDetailButton = itemView.findViewById(R.id.search_cookie_info_button);
            titleTextView = itemView.findViewById(R.id.search_page_titleText);
            releaseDateTextView = itemView.findViewById(R.id.search_releaseDate);
            voteAverageTextView = itemView.findViewById(R.id.search_rate);
            posterImageView = itemView.findViewById(R.id.search_posterImageView);
        }
    }
}

