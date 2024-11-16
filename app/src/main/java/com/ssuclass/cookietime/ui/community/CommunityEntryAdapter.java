package com.ssuclass.cookietime.ui.community;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.databinding.ViewholderCommunityEntryBinding;

import java.util.List;

public class CommunityEntryAdapter extends RecyclerView.Adapter<CommunityEntryAdapter.CommunityEntryViewHolder> {

    // Fields
    private final List<String> data;

    // Constructor
    public CommunityEntryAdapter(List<String> data) {
        this.data = data;
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
        String movieTitle = data.get(position);
        holder.binding.movieTitleTextview.setText(movieTitle);

        setOnClickListener(holder);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    private void setOnClickListener(CommunityEntryViewHolder holder) {
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, CommunityDetailActivity.class);
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
