package com.ssuclass.cookietime.presentation.cookieinfo;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.domain.CookieKeywordModel;

import java.util.ArrayList;
import java.util.List;

public class KeywordAdapter extends RecyclerView.Adapter<KeywordAdapter.KeywordViewHolder> {

    private List<CookieKeywordModel> keywordList;

    public KeywordAdapter(List<CookieKeywordModel> keywordList) {
        this.keywordList = keywordList != null ? keywordList : new ArrayList<>();
    }

    // 데이터 업데이트 메서드
    public void updateKeywords(List<CookieKeywordModel> newKeywordList) {
        if (newKeywordList == null) {
            newKeywordList = new ArrayList<>();
        }

        this.keywordList = newKeywordList;
        notifyDataSetChanged(); // 데이터 변경 사항 반영
    }

    // 키워드를 정렬하는 메서드
    public List<CookieKeywordModel> getTopKeywords() {
        if (keywordList == null || keywordList.isEmpty()) {
            return new ArrayList<>(); // 빈 리스트 반환
        }

        // 리스트 정렬 (예: 빈도 순)
        keywordList.sort((k1, k2) -> Integer.compare(k2.getCount(), k1.getCount()));

        // 상위 5개의 키워드 반환
        return keywordList.size() > 5 ? keywordList.subList(0, 5) : keywordList;
    }

    @NonNull
    @Override
    public KeywordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cookie_survey_keyword, parent, false);
        return new KeywordViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull KeywordViewHolder holder, int position) {
        CookieKeywordModel keywordItem = keywordList.get(position);
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
}
