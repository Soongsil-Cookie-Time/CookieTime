package com.ssuclass.cookietime.presentation.badgemanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.databinding.ItemBadgeBinding;

public class ItemMonthlyBadgesAdapter extends RecyclerView.Adapter<ItemMonthlyBadgesAdapter.ItemMonthlyBadgesViewHolder> {

    private final Context context;

    public ItemMonthlyBadgesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ItemMonthlyBadgesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBadgeBinding binding = ItemBadgeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemMonthlyBadgesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemMonthlyBadgesViewHolder holder, int position) {
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InstagramSharingActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ItemMonthlyBadgesViewHolder extends RecyclerView.ViewHolder {
        private final ItemBadgeBinding binding;

        public ItemMonthlyBadgesViewHolder(@NonNull ItemBadgeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
