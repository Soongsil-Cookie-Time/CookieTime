package com.ssuclass.cookietime.presentation.community.comments;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.databinding.ItemCommentBinding;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentItemViewHolder> {
    private final ArrayList<CommentsModel> dataList;
    private ItemCommentBinding binding;

    public CommentsAdapter(ArrayList<CommentsModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public CommentItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new CommentItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentItemViewHolder holder, int position) {
        binding.contentTextview.setText(dataList.get(position).getTitle());
        binding.nicknameTextview.setText(dataList.get(position).getNickname());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class CommentItemViewHolder extends RecyclerView.ViewHolder {
        ItemCommentBinding binding;

        public CommentItemViewHolder(@NonNull ItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
