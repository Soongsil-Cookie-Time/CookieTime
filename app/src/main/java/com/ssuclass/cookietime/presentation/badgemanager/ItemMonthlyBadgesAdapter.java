package com.ssuclass.cookietime.presentation.badgemanager;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.databinding.ItemBadgeBinding;

public class ItemMonthlyBadgesAdapter extends RecyclerView.Adapter<ItemMonthlyBadgesAdapter.ItemMonthlyBadgesViewHolder> {
    @NonNull
    @Override
    public ItemMonthlyBadgesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBadgeBinding binding = ItemBadgeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemMonthlyBadgesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemMonthlyBadgesViewHolder holder, int position) {
        // TODO: 실질적으로 배지 처리되는 코드
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
