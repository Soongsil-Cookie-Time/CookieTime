package com.ssuclass.cookietime.presentation.home;

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
import com.ssuclass.cookietime.network.response.TMDBNowPlayingResponse;

import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {

    private final List<TMDBNowPlayingResponse.Movie> dataList;
    private final OnCookieButtonClickListener listener;

    public CarouselAdapter(List<TMDBNowPlayingResponse.Movie> dataList, OnCookieButtonClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_boxoffice, parent, false);
        return new CarouselViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        TMDBNowPlayingResponse.Movie data = dataList.get(position);
        holder.titleText.setText(data.getTitle());
        holder.rankText.setText(String.valueOf(position + 1));
        holder.rateText.setText(String.format("%.1f", data.getVoteAverage()));
        Glide.with(holder.itemView.getContext()).load("https://image.tmdb.org/t/p/w500" + data.getPosterPath()).into(holder.posterImage);
        holder.cookieButton.setOnClickListener(v -> listener.onCookieButtonClick(data));
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0; // Null 체크
    }


    public static class CarouselViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        ImageView posterImage;
        TextView rankText;
        Button cookieButton;
        TextView rateText;

        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            rateText = itemView.findViewById(R.id.rate_Text);
            posterImage = itemView.findViewById(R.id.boxoffice_posterImageView);
            titleText = itemView.findViewById(R.id.boxoffice_titleText);
            rankText = itemView.findViewById(R.id.boxoffice_rankText);
            cookieButton = itemView.findViewById(R.id.boxoffice_go_to_cookie_detail_button);
        }
    }
}
