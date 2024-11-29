package com.ssuclass.cookietime.presentation.mypage.friendlist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.databinding.ItemFriendInfoBinding;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendListViewHolder> {
    ItemFriendInfoBinding binding;

    @NonNull
    @Override
    public FriendListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemFriendInfoBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new FriendListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class FriendListViewHolder extends RecyclerView.ViewHolder {
        ItemFriendInfoBinding binding;

        public FriendListViewHolder(@NonNull ItemFriendInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
