package com.ssuclass.cookietime.ui.community;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.databinding.ViewholderCommunityDetailBinding;
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
        ViewholderCommunityDetailBinding binding = ViewholderCommunityDetailBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CommunityDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityDetailViewHolder holder, int position) {
        String title = dataList.get(position).getTitle();
        String content = dataList.get(position).getContent();

        holder.binding.titleTextview.setText(title);
        holder.binding.contentTextview.setText(content);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class CommunityDetailViewHolder extends RecyclerView.ViewHolder {
        ViewholderCommunityDetailBinding binding;

        public CommunityDetailViewHolder(ViewholderCommunityDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
