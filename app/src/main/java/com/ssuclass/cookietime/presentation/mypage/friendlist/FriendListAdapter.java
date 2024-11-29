package com.ssuclass.cookietime.presentation.mypage.friendlist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.databinding.ItemFriendInfoBinding;

import java.util.ArrayList;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendListViewHolder> {
    private final ArrayList<FriendListModel> dataList;
    private ItemFriendInfoBinding binding;

    public FriendListAdapter(ArrayList<FriendListModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public FriendListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemFriendInfoBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new FriendListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListViewHolder holder, int position) {
        binding.friendNameTextview.setText(dataList.get(position).getFriendName());
        binding.nicknameTextview.setText(dataList.get(position).getNickname());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class FriendListViewHolder extends RecyclerView.ViewHolder {
        ItemFriendInfoBinding binding;

        public FriendListViewHolder(@NonNull ItemFriendInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
