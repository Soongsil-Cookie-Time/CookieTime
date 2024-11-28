package com.ssuclass.cookietime.presentation.community;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.databinding.ItemCommunityDetailBinding;
import com.ssuclass.cookietime.domain.CommunityDetailModel;

import java.util.List;

public class CommunityDetailAdapter extends RecyclerView.Adapter<CommunityDetailAdapter.CommunityDetailViewHolder> {
    private final List<CommunityDetailModel> dataList;

    public CommunityDetailAdapter(List<CommunityDetailModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public CommunityDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCommunityDetailBinding binding = ItemCommunityDetailBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CommunityDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityDetailViewHolder holder, int position) {
        String title = dataList.get(position).getTitle();
        holder.binding.titleTextview.setText(title);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class CommunityDetailViewHolder extends RecyclerView.ViewHolder {
        ItemCommunityDetailBinding binding;

        public CommunityDetailViewHolder(ItemCommunityDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
