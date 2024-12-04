package com.ssuclass.cookietime.presentation.badgemanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.databinding.ItemBadgeBinding;
import com.ssuclass.cookietime.presentation.badgemanager.model.BadgeModel;

import java.util.List;

public class ItemMonthlyBadgesAdapter extends RecyclerView.Adapter<ItemMonthlyBadgesAdapter.ItemMonthlyBadgesViewHolder> {

    private final Context context;
    private final List<BadgeModel> monthlyMovies;  // 해당 월의 영화 데이터 리스트

    // 생성자 수정: 영화 데이터 리스트를 받도록 함
    public ItemMonthlyBadgesAdapter(Context context, List<BadgeModel> monthlyMovies) {
        this.context = context;
        this.monthlyMovies = monthlyMovies;
    }

    @NonNull
    @Override
    public ItemMonthlyBadgesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 기존 코드와 동일
        ItemBadgeBinding binding = ItemBadgeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemMonthlyBadgesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemMonthlyBadgesViewHolder holder, int position) {
        // 현재 위치의 영화 데이터 가져오기
        BadgeModel movie = monthlyMovies.get(position);

        // 영화 제목을 TextView에 설정
        holder.binding.badgeMovieTitleTextview.setText(movie.getTitle());

        // 클릭 이벤트에 영화 정보 전달
        holder.binding.getRoot().setOnClickListener(view -> {
            Intent intent = new Intent(context, InstagramSharingActivity.class);
            // 인스타그램 공유 화면에 영화 제목 전달
            intent.putExtra("movie_title", movie.getTitle());
            intent.putExtra("movie_id", movie.getMovieId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // 실제 영화 데이터 개수 반환
        return monthlyMovies.size();
    }

    public static class ItemMonthlyBadgesViewHolder extends RecyclerView.ViewHolder {
        private final ItemBadgeBinding binding;

        public ItemMonthlyBadgesViewHolder(@NonNull ItemBadgeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}