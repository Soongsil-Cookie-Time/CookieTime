package com.ssuclass.cookietime.presentation.badgemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.databinding.ItemMonthyBadgesBinding;
import com.ssuclass.cookietime.presentation.badgemanager.model.MonthGroup;

import java.util.List;

public class FragmentBadgeManagerAdapter extends RecyclerView.Adapter<FragmentBadgeManagerAdapter.FragmentBadgeManagerViewHolder> {

    private final Context context;
    private final List<MonthGroup> monthGroups;  // 월별 그룹 리스트로 변경
    ItemMonthyBadgesBinding binding;

    public FragmentBadgeManagerAdapter(Context context, List<MonthGroup> monthGroups) {
        this.context = context;
        this.monthGroups = monthGroups;
    }

    @NonNull
    @Override
    public FragmentBadgeManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemMonthyBadgesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FragmentBadgeManagerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentBadgeManagerViewHolder holder, int position) {
        MonthGroup currentMonth = monthGroups.get(position);

        // 월 표시 설정
        holder.binding.monthlyTitleTextview.setText(
                String.format("%s년 %s월", currentMonth.getYear(), currentMonth.getMonth())
        );

        // 해당 월의 영화들로 어댑터 생성
        ItemMonthlyBadgesAdapter adapter = new ItemMonthlyBadgesAdapter(context, currentMonth.getMovies());
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
        return monthGroups.size();  // 월 그룹의 개수만 반환
    }

    public static class FragmentBadgeManagerViewHolder extends RecyclerView.ViewHolder {
        ItemMonthyBadgesBinding binding;

        public FragmentBadgeManagerViewHolder(@NonNull ItemMonthyBadgesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
