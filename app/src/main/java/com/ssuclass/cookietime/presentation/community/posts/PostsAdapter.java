package com.ssuclass.cookietime.presentation.community.posts;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.databinding.ItemCommunityDetailBinding;
import com.ssuclass.cookietime.presentation.community.comments.CommentsActivity;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.CommunityDetailViewHolder> {
    private final List<PostsModel> dataList;
    private final String movieId;
    private ItemCommunityDetailBinding binding;

    public PostsAdapter(List<PostsModel> dataList, String movieId) {
        this.dataList = dataList;
        this.movieId = movieId;
    }

    @NonNull
    @Override
    public CommunityDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemCommunityDetailBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CommunityDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityDetailViewHolder holder, int position) {
        String title = dataList.get(position).getTitle();
        holder.binding.titleTextview.setText(title);
        addViewHolderListener(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void addViewHolderListener(int position) {
        binding.getRoot().setOnClickListener(view -> {
            Intent intent = new Intent(binding.getRoot().getContext(), CommentsActivity.class);
            intent.putExtra("movieId", this.movieId);
            intent.putExtra("postId", dataList.get(position).getPostId());
            binding.getRoot().getContext().startActivity(intent);
        });
    }

    public static class CommunityDetailViewHolder extends RecyclerView.ViewHolder {
        ItemCommunityDetailBinding binding;

        public CommunityDetailViewHolder(ItemCommunityDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
