package com.ssuclass.cookietime.presentation.badgemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.databinding.ItemMonthyBadgesBinding;

import java.util.ArrayList;

public class FragmentBadgeManagerAdapter extends RecyclerView.Adapter<FragmentBadgeManagerAdapter.FragmentBadgeManagerViewHolder> {

    private final Context context;
    private final ArrayList<BadgeModel> dataList;
    ItemMonthyBadgesBinding binding;

    public FragmentBadgeManagerAdapter(Context context, ArrayList<BadgeModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public FragmentBadgeManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemMonthyBadgesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FragmentBadgeManagerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentBadgeManagerViewHolder holder, int position) {
        ItemMonthlyBadgesAdapter adapter = new ItemMonthlyBadgesAdapter(this.context);
        setMonthTextView(position);
        RecyclerView recyclerView = holder.binding.badgesRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(holder.binding.getRoot().getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void setMonthTextView(int position) {
        binding.monthlyTitleTextview.setText(dataList.get(position).getMonth() + "월");
    }

    public static class FragmentBadgeManagerViewHolder extends RecyclerView.ViewHolder {
        ItemMonthyBadgesBinding binding;

        public FragmentBadgeManagerViewHolder(@NonNull ItemMonthyBadgesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
