package com.ssuclass.cookietime.presentation.badgemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.databinding.ItemMonthyBadgesBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        // 현재 월의 영화 목록 필터링
        String currentMonth = dataList.get(position).getMonth();
        String currentYear = dataList.get(position).getYear();

        // 현재 년도와 월에 해당하는 영화들만 필터링
        List<BadgeModel> monthlyMovies = dataList.stream()
                .filter(movie -> movie.getYear().equals(currentYear)
                        && movie.getMonth().equals(currentMonth))
                .collect(Collectors.toList());

        // 월 표시 설정
        setMonthTextView(position);

        // 필터링된 영화 목록으로 어댑터 생성
        ItemMonthlyBadgesAdapter adapter = new ItemMonthlyBadgesAdapter(context, monthlyMovies);

        RecyclerView recyclerView = holder.binding.badgesRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(
                holder.binding.getRoot().getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        ));
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
