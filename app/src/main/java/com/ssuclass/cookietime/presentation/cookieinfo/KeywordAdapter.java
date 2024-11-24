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

import java.util.List;

public class KeywordAdapter extends RecyclerView.Adapter<KeywordAdapter.KeywordViewHolder> {

    private final List<CookieKeywordModel> keywordList;

    public KeywordAdapter(List<CookieKeywordModel> keywordList) {
        this.keywordList = keywordList;
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
