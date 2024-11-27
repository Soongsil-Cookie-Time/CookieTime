package com.ssuclass.cookietime.presentation.cookieinfo;

import android.graphics.drawable.GradientDrawable;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.domain.CookieKeywordModel;

import java.util.ArrayList;
import java.util.List;

public class KeywordAdapter extends RecyclerView.Adapter<KeywordAdapter.KeywordViewHolder> {

    private List<CookieKeywordModel> keywordList;

    public KeywordAdapter(List<CookieKeywordModel> keywordList) {
        this.keywordList = keywordList;
    }

    // 데이터 업데이트 메서드
    public void updateKeywords(List<CookieKeywordModel> newKeywordList) {
        this.keywordList = getTopKeywords(newKeywordList);
        notifyDataSetChanged(); // 데이터 변경 사항 반영
    }

    // 키워드를 정렬하는 메서드
    public List<CookieKeywordModel> getTopKeywords(List<CookieKeywordModel> newKeywordList) {
        // 필터링된 리스트를 저장할 새로운 리스트 생성
        List<CookieKeywordModel> filteredList = new ArrayList<>();

        // count가 0보다 큰 항목만 추가
        for (CookieKeywordModel keyword : newKeywordList) {
            if (keyword.getCount() > 0) {
                filteredList.add(keyword);
            }
        }

        // 리스트 정렬 (예: 빈도 순)
        filteredList.sort((k1, k2) -> Integer.compare(k2.getCount(), k1.getCount()));

        // 상위 5개의 키워드 반환
        return filteredList.size() > 5 ? filteredList.subList(0, 5) : filteredList;
    }


    @NonNull
    @Override
    public KeywordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cookie_survey_keyword, parent, false);
        return new KeywordViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull KeywordViewHolder holder, int position) {
        CookieKeywordModel keywordItem = keywordList.get(position);

        int backgroundColor = ContextCompat.getColor(
                holder.itemView.getContext(),
                keywordItem.getPositive() ? R.color.primary : R.color.pink
        );

        setBackgroundWithRadius(holder.itemView, backgroundColor, 16f);
        holder.keywordText.setText(keywordItem.getKeyword());
        holder.keywordCount.setText(String.format("%d명", keywordItem.getCount()));
    }



    @Override
    public int getItemCount() {
        return keywordList.size();
    }

    static class KeywordViewHolder extends RecyclerView.ViewHolder {
        TextView keywordText;
        TextView keywordCount;

        public KeywordViewHolder(@NonNull View itemView) {
            super(itemView);
            keywordText = itemView.findViewById(R.id.survey_keyword_text);
            keywordCount = itemView.findViewById(R.id.survey_keyword_count);
        }
    }

    // 색상과 cornerRadius를 동적으로 설정하는 메서드
    private void setBackgroundWithRadius(View view, int color, float cornerRadius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 모양 설정 (사각형)
        drawable.setColor(color);                     // 색상 설정
        drawable.setCornerRadius(cornerRadius);       // 코너 라디우스 설정

        view.setBackground(drawable);                 // 배경에 적용
    }
}
