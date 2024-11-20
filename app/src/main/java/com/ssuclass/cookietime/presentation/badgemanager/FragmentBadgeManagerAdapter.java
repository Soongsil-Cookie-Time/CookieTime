package com.ssuclass.cookietime.presentation.badgemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.databinding.ItemMonthyBadgesBinding;

public class FragmentBadgeManagerAdapter extends RecyclerView.Adapter<FragmentBadgeManagerAdapter.FragmentBadgeManagerViewHolder> {

    private final Context context;

    public FragmentBadgeManagerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public FragmentBadgeManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMonthyBadgesBinding binding = ItemMonthyBadgesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FragmentBadgeManagerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentBadgeManagerViewHolder holder, int position) {
        ItemMonthlyBadgesAdapter adapter = new ItemMonthlyBadgesAdapter(this.context);
        RecyclerView recyclerView = holder.binding.badgesRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(holder.binding.getRoot().getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class FragmentBadgeManagerViewHolder extends RecyclerView.ViewHolder {
        ItemMonthyBadgesBinding binding;

        public FragmentBadgeManagerViewHolder(@NonNull ItemMonthyBadgesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
