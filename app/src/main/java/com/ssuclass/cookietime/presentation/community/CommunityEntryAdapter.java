package com.ssuclass.cookietime.presentation.community;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ssuclass.cookietime.databinding.ItemCommunityEntryBinding;
import com.ssuclass.cookietime.domain.CommunityEntryModel;

import java.util.List;

public class CommunityEntryAdapter extends RecyclerView.Adapter<CommunityEntryAdapter.CommunityEntryViewHolder> {

    // Fields
    private final List<CommunityEntryModel> dataList;
    ItemCommunityEntryBinding binding;

    // Constructor
    public CommunityEntryAdapter(List<CommunityEntryModel> dataList) {
        this.dataList = dataList;
    }

    // Methods
    @NonNull
    @Override
    public CommunityEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemCommunityEntryBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CommunityEntryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityEntryViewHolder holder, int position) {
        String movieTitle = dataList.get(position).getTitle();
        holder.binding.movieTitleTextview.setText(movieTitle);

        String posterUrl = "https://image.tmdb.org/t/p/w500" + dataList.get(position).getMoviePosterUrl();
        Log.d("Poster", posterUrl);
        Glide.with(binding.getRoot().getContext())
                .load(posterUrl)
                .into(holder.binding.moviePosterImageview);

        implementViewHolderListener(holder);
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }

    private void implementViewHolderListener(CommunityEntryViewHolder holder) {
        holder.binding.getRoot().setOnClickListener(view -> {
            int position = holder.getAdapterPosition();
            String communityId = dataList.get(position).getId();

            Context context = view.getContext();
            Intent intent = new Intent(context, CommunityDetailActivity.class);
            intent.putExtra("movieId", communityId);
            context.startActivity(intent);
        });
    }

    // Nested Class -> Field
    public static class CommunityEntryViewHolder extends RecyclerView.ViewHolder {
        ItemCommunityEntryBinding binding;

        public CommunityEntryViewHolder(ItemCommunityEntryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
