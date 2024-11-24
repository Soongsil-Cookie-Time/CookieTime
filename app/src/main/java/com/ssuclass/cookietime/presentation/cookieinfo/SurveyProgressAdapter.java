package com.ssuclass.cookietime.presentation.cookieinfo;

import android.annotation.SuppressLint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.domain.SurveyProgressModel;

import java.util.List;

public class SurveyProgressAdapter extends RecyclerView.Adapter<SurveyProgressAdapter.SurveyViewHolder> {

    private final SurveyProgressModel surveyList;

    public SurveyProgressAdapter(SurveyProgressModel surveyList) {
        this.surveyList = surveyList;
    }

    @NonNull
    @Override
    public SurveyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cookie_survey_progress_bar, parent, false);
        return new SurveyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SurveyViewHolder holder, int position) {
        switch (position) {
            case 0: // 첫 번째 아이템 (예: 쿠키 개수)
                // 쿠키 개수 응답 값들
                int[] cookieCounts = {
                        surveyList.getCookieZeroCount(),
                        surveyList.getCookieOneCount(),
                        surveyList.getCookieTwoCount(),
                        surveyList.getCookieThreeCount()
                };

                // 가장 높은 응답 값 및 인덱스 찾기
                int[] topTwoIndexes = findTopTwoIndexes(cookieCounts);
                int topIndex1 = topTwoIndexes[0];
                int topIndex2 = topTwoIndexes[1];

                // 총합 계산
                int totalCookieCount = cookieCounts[topIndex1] + cookieCounts[topIndex2];
                int topPercent1 = calculatePercentage(cookieCounts[topIndex1], totalCookieCount);
                int topPercent2 = calculatePercentage(cookieCounts[topIndex2], totalCookieCount);

                // 텍스트 표시
                String[] labels = {"0개", "1개", "2개", "3개"};
                String titleText = "쿠키 개수 : ";
                String dynamicText = labels[topIndex1];

                // 텍스트뷰에 부분 색상 적용
                setColoredText(holder.title, titleText, dynamicText);
                holder.startText.setText(labels[topIndex1] + " " + topPercent1 + "%");
                holder.endText.setText(labels[topIndex2] + " " + topPercent2 + "%");

                // ProgressBar 설정
                holder.progressBar.setProgress(topPercent1);
                break;

            case 1: // 두 번째 아이템 (예: 쿠키 길이)
                // 쿠키 길이 데이터
                int totalCookieLength = surveyList.getCookieLongCount() + surveyList.getCookieShortCount();
                int cookieLongPercent = calculatePercentage(surveyList.getCookieLongCount(), totalCookieLength);
                int cookieShortPercent = calculatePercentage(surveyList.getCookieShortCount(), totalCookieLength);

                // 가장 높은 투표율 기준으로 제목 설정
                String cookieLengthTitle = (surveyList.getCookieLongCount() >= surveyList.getCookieShortCount())
                        ? "길어요" : "짧아요";
                // 텍스트뷰에 부분 색상 적용
                setColoredText(holder.title, "쿠키 길이 : ", cookieLengthTitle);

                holder.startText.setText("길어요 " + cookieLongPercent + "%");
                holder.endText.setText("짧아요 " + cookieShortPercent + "%");

                // ProgressBar 설정
                holder.progressBar.setProgress(cookieLongPercent);
                break;

            case 2: // 세 번째 아이템 (예: 쿠키 중요도)
                // 쿠키 중요도 데이터
                int totalCookieImportance = surveyList.getCookieImportantCount() + surveyList.getCookieNotImportantCount();
                int cookieImportantPercent = calculatePercentage(surveyList.getCookieImportantCount(), totalCookieImportance);
                int cookieNotImportantPercent = calculatePercentage(surveyList.getCookieNotImportantCount(), totalCookieImportance);

                // 가장 높은 투표율 기준으로 제목 설정
                String cookieImportanceTitle = (surveyList.getCookieImportantCount() >= surveyList.getCookieNotImportantCount())
                        ? "중요해요" : "중요하지 않아요";
                // 텍스트뷰에 부분 색상 적용
                setColoredText(holder.title, "쿠키 중요도 : ", cookieImportanceTitle);

                holder.startText.setText("중요해요 " + cookieImportantPercent + "%");
                holder.endText.setText("중요하지 않아요 " + cookieNotImportantPercent + "%");

                // ProgressBar 설정
                holder.progressBar.setProgress(cookieImportantPercent);
                break;

            default: // 기본 처리 (포지션이 예상 범위 바깥인 경우)
                holder.title.setText("알 수 없는 데이터");
                holder.progressBar.setProgress(0);
                holder.startText.setText("-");
                holder.endText.setText("-");
                break;
        }
    }
    private void setColoredText(TextView textView, String staticText, String dynamicText) {
        // 전체 문자열 결합
        String fullText = staticText + dynamicText;

        // SpannableString을 사용하여 특정 부분에 색상 적용
        SpannableString spannableString = new SpannableString(fullText);

        // staticText 부분은 검정색 유지
        spannableString.setSpan(
                new ForegroundColorSpan(textView.getContext().getResources().getColor(android.R.color.black)),
                0,
                staticText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // dynamicText 부분은 @primary 색상 적용
        spannableString.setSpan(
                new ForegroundColorSpan(textView.getContext().getResources().getColor(R.color.primary)),
                staticText.length(),
                fullText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // TextView에 적용
        textView.setText(spannableString);
    }



    private int[] findTopTwoIndexes(int[] array) {
        int max1 = -1, max2 = -1; // 가장 큰 값, 두 번째로 큰 값
        int index1 = -1, index2 = -1; // 가장 큰 값의 인덱스, 두 번째로 큰 값의 인덱스

        for (int i = 0; i < array.length; i++) {
            if (array[i] > max1) {
                max2 = max1;
                index2 = index1;
                max1 = array[i];
                index1 = i;
            } else if (array[i] > max2) {
                max2 = array[i];
                index2 = i;
            }
        }

        return new int[]{index1, index2};
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    private int calculatePercentage(int part, int total) {
        if (total == 0) return 0; // 분모가 0인 경우
        return (int) ((part / (float) total) * 100);
    }

    static class SurveyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ProgressBar progressBar;
        TextView startText;
        TextView endText;

        public SurveyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cookie_info_title);
            progressBar = itemView.findViewById(R.id.cookie_info_progress_bar);
            startText = itemView.findViewById(R.id.cookie_info_start_text);
            endText = itemView.findViewById(R.id.cookie_info_end_text);
        }
    }
}
