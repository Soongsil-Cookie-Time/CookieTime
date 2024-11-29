package com.ssuclass.cookietime.presentation.community.entry;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ssuclass.cookietime.databinding.ItemCommunityEntryBinding;
import com.ssuclass.cookietime.presentation.community.posts.PostsActivity;

import java.util.List;

public class CommunitiesAdapter extends RecyclerView.Adapter<CommunitiesAdapter.CommunityEntryViewHolder> {

    // Fields
    private final List<CommunitiesModel> dataList;
    ItemCommunityEntryBinding binding;

    // Constructor
    public CommunitiesAdapter(List<CommunitiesModel> dataList) {
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
            String movieId = dataList.get(position).getId();

            Context context = view.getContext();
            Intent intent = new Intent(context, PostsActivity.class);
            intent.putExtra("movieId", movieId);
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
