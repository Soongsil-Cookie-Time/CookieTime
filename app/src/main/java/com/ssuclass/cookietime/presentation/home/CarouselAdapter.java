package com.ssuclass.cookietime.presentation.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.domain.BoxOfficeDataModel;

import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {

    private final List<BoxOfficeDataModel> dataList;
    private final OnCookieButtonClickListener listener;

    public CarouselAdapter(List<BoxOfficeDataModel> dataList, OnCookieButtonClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_boxoffice, parent, false);
        return new CarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        BoxOfficeDataModel data = dataList.get(position);
        // TODO: 이미지 로드
        holder.titleText.setText(data.getTitle());
        holder.rankText.setText(position + 1);
        holder.cookieButton.setOnClickListener(v -> listener.onCookieButtonClick(data));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class CarouselViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        ImageView posterImage;
        TextView rankText;
        Button cookieButton;

        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.boxoffice_posterImageView);
            titleText = itemView.findViewById(R.id.boxoffice_titleText);
            rankText = itemView.findViewById(R.id.boxoffice_rankText);
            cookieButton = itemView.findViewById(R.id.boxoffice_go_to_cookie_detail_button);
        }
    }
}
