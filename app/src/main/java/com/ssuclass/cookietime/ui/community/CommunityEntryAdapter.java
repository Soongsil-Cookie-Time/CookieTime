package com.ssuclass.cookietime.ui.community;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.databinding.ViewholderCommunityEntryBinding;
import com.ssuclass.cookietime.domain.CommunityEntryModel;

import java.util.List;

public class CommunityEntryAdapter extends RecyclerView.Adapter<CommunityEntryAdapter.CommunityEntryViewHolder> {

    // Fields
    private List<CommunityEntryModel> dataList;

    // Constructor
    public CommunityEntryAdapter(List<CommunityEntryModel> dataList) {
        this.dataList = dataList;
    }

    // Methods
    @NonNull
    @Override
    public CommunityEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCommunityEntryBinding binding = ViewholderCommunityEntryBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CommunityEntryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityEntryViewHolder holder, int position) {
        String movieTitle = dataList.get(position).getTitle();
        holder.binding.movieTitleTextview.setText(movieTitle);

        setOnClickListener(holder);
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }

    public void updateDataList(List<CommunityEntryModel> dataList) {
        this.dataList = dataList;
    }

    private void setOnClickListener(CommunityEntryViewHolder holder) {
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                String communityId = dataList.get(position).getId();

                Context context = view.getContext();
                Intent intent = new Intent(context, CommunityDetailActivity.class);
                intent.putExtra("communityId", communityId);
                context.startActivity(intent);
            }
        });
    }

    // Nested Class -> Field
    public static class CommunityEntryViewHolder extends RecyclerView.ViewHolder {
        ViewholderCommunityEntryBinding binding;

        public CommunityEntryViewHolder(ViewholderCommunityEntryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
