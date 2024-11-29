package com.ssuclass.cookietime.presentation.community.posts;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.databinding.ItemCommunityDetailBinding;
import com.ssuclass.cookietime.presentation.community.comments.CommentsActivity;
import com.ssuclass.cookietime.util.FirebaseConstants;

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
        binding.titleTextview.setText(dataList.get(position).getTitle());
        binding.contentTextview.setText(dataList.get(position).getContent());
        binding.nicknameTextview.setText("| " + dataList.get(position).getNickname());
        addViewHolderListener(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void addViewHolderListener(int position) {
        binding.getRoot().setOnClickListener(view -> {
            Intent intent = new Intent(binding.getRoot().getContext(), CommentsActivity.class);
            intent.putExtra(FirebaseConstants.MOVIEID_DOCUMENT, this.movieId);
            intent.putExtra("title", dataList.get(position).getTitle());
            intent.putExtra("content", dataList.get(position).getContent());
            intent.putExtra("postId", dataList.get(position).getPostId());
            intent.putExtra("nickname", dataList.get(position).getNickname());
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
