package com.ssuclass.cookietime.presentation.survey;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.domain.CookieKeywordModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SurveyKeywordAdapter extends RecyclerView.Adapter<SurveyKeywordAdapter.KeywordViewHolder> {

    private final Set<Integer> selectedPositions = new HashSet<>(); // 복수 선택된 셀의 위치를 저장
    private final List<CookieKeywordModel> keywordList;
    private final KeywordSelectionListener selectionListener; // 인터페이스를 통해 선택 이벤트 전달

    public SurveyKeywordAdapter(List<CookieKeywordModel> keywordList, KeywordSelectionListener selectionListener) {
        this.keywordList = keywordList;
        this.selectionListener = selectionListener;
    }

    @NonNull
    @Override
    public SurveyKeywordAdapter.KeywordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cookie_survey_keyword, parent, false);
        return new SurveyKeywordAdapter.KeywordViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull SurveyKeywordAdapter.KeywordViewHolder holder, int position) {
        CookieKeywordModel keywordItem = keywordList.get(position);

        // 선택 상태에 따른 배경 색상 설정
        int backgroundColor;
        if (selectedPositions.contains(position)) {
            backgroundColor = ContextCompat.getColor(
                    holder.itemView.getContext(),
                    keywordItem.getPositive() ? R.color.primary : R.color.pink
            );
        } else {
            backgroundColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.gray); // #D9D9D9
        }

        setBackgroundWithRadius(holder.itemView, backgroundColor, 16f);

        // 데이터 설정
        holder.keywordText.setText(keywordItem.getKeyword());
        holder.keywordCount.setText("");

        // 클릭 이벤트 처리
        holder.itemView.setOnClickListener(v -> {
            if (selectedPositions.contains(position)) {
                selectedPositions.remove(position); // 선택 해제
            } else {
                selectedPositions.add(position); // 선택 추가
                keywordItem.setCount(keywordItem.getCount() + 1); // 모델의 count 증가
                if (selectionListener != null) {
                    selectionListener.onKeywordSelected(getFieldNameForKeyword(keywordItem)); // 선택 이벤트 전달
                }
            }
            notifyItemChanged(position); // 변경된 항목만 업데이트
        });
    }

    @Override
    public int getItemCount() {
        return keywordList.size();
    }

    // 색상과 cornerRadius를 동적으로 설정하는 메서드
    private void setBackgroundWithRadius(View view, int color, float cornerRadius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 모양 설정 (사각형)
        drawable.setColor(color);                     // 색상 설정
        drawable.setCornerRadius(cornerRadius);       // 코너 라디우스 설정
        view.setBackground(drawable);                 // 배경에 적용
    }

    // 키워드와 Firestore 필드 이름을 매핑하는 메서드
    private String getFieldNameForKeyword(CookieKeywordModel keywordItem) {
        switch (keywordItem.getKeyword()) {
            case "엔딩 크레딧 이후 오래 기다렸어요":
                return "cookieWaitLong";
            case "보길 잘했어요":
                return "cookieWorthWatching";
            case "다음 시리즈 떡밥이 포함되어 있어요":
                return "cookieNextSeries";
            case "괜히 봤어요":
                return "cookieRegret";
            case "엔딩 크레딧 직후 바로 나왔어요":
                return "cookieQuickExit";
            case "쿠키 내용이 재밌어요":
                return "cookieContentFun";
            case "이스터에그가 포함되어 있어요":
                return "cookieEasterEgg";
            default:
                return null;
        }
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
