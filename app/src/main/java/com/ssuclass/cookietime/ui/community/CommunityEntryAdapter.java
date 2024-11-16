package com.ssuclass.cookietime.ui.community;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.databinding.ViewholderCommunityItemBinding;

import java.util.List;

public class CommunityEntryAdapter extends RecyclerView.Adapter<CommunityEntryAdapter.CommunityEntryViewHolder> {

    // Fields
    private final List<String> data;

    public CommunityEntryAdapter(List<String> data) {
        this.data = data;
    }

    // Methods
    @NonNull
    @Override
    public CommunityEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCommunityItemBinding binding = ViewholderCommunityItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CommunityEntryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityEntryViewHolder holder, int position) {
        String movieTitle = data.get(position);
        holder.binding.movieTitleTextview.setText(movieTitle);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    // Nested Class -> Field
    public static class CommunityEntryViewHolder extends RecyclerView.ViewHolder {
        ViewholderCommunityItemBinding binding;

        public CommunityEntryViewHolder(ViewholderCommunityItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
