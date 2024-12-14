package com.ssuclass.cookietime.presentation.cookieinfo;

import android.annotation.SuppressLint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.domain.SurveyProgressModel;

public class SurveyProgressAdapter extends RecyclerView.Adapter<SurveyProgressAdapter.SurveyViewHolder> {

    private static final String TAG = "SurveyProgressAdapter"; // 디버깅 태그
    private SurveyProgressModel surveyList;


    public SurveyProgressAdapter(SurveyProgressModel surveyList) {
        this.surveyList = surveyList;
    }

    // 데이터 업데이트 메서드
    public void updateSurveyData(SurveyProgressModel newSurveyList) {
        Log.d(TAG, "updateSurveyData: New survey list received: " + newSurveyList);
        this.surveyList = newSurveyList;
        notifyDataSetChanged(); // 데이터 변경 사항 반영
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
        Log.d(TAG, "onBindViewHolder: Binding data at position: " + position);
        // 1. 모든 뷰 초기화
        holder.progressBar.setProgress(0); // ProgressBar 초기화
        holder.startText.setText("");     // 시작 텍스트 초기화
        holder.endText.setText("");       // 종료 텍스트 초기화
        holder.title.setText("");         // 제목 초기화

        // 2. position에 따라 데이터 처리
        switch (position) {
            case 0: // 첫 번째 아이템: 쿠키 개수
                updateCookieCount(holder);
                break;

            case 1: // 두 번째 아이템: 쿠키 길이
                updateCookieLength(holder);
                break;

            case 2: // 세 번째 아이템: 쿠키 중요도
                updateCookieImportance(holder);
                break;

            default: // 예상치 못한 포지션
                holder.title.setText("알 수 없는 데이터");
                holder.progressBar.setProgress(0);
                holder.startText.setText("-");
                holder.endText.setText("-");
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateCookieCount(SurveyViewHolder holder) {
        // 쿠키 개수 데이터 계산
        int[] cookieCounts = {
                surveyList.getCookieZeroCount(),
                surveyList.getCookieOneCount(),
                surveyList.getCookieTwoCount(),
                surveyList.getCookieThreeCount()
        };

        Log.d(TAG, "updateCookieCount: Raw cookie count data: " + java.util.Arrays.toString(cookieCounts));

        int totalCookieCount = 0;
        for (int count : cookieCounts) {
            totalCookieCount += count;
        }

        Log.d(TAG, "updateCookieCount: Total cookie count: " + totalCookieCount);

        // 가장 높은 응답 값 및 인덱스 찾기
        int[] topTwoIndexes = findTopTwoIndexes(cookieCounts);
        int topIndex1 = topTwoIndexes[0];
        int topIndex2 = topTwoIndexes[1];
        int topPercent1 = calculatePercentage(cookieCounts[topIndex1], totalCookieCount);
        int topPercent2 = calculatePercentage(cookieCounts[topIndex2], totalCookieCount);

        Log.d(TAG, "updateCookieCount: Top indexes: " + java.util.Arrays.toString(topTwoIndexes));
        Log.d(TAG, "updateCookieCount: Top percentages: " + topPercent1 + "%, " + topPercent2 + "%");

        // 라벨 설정
        String[] labels = {"0개", "1개", "2개", "3개"};
        holder.progressBar.setProgress(topPercent1);
        holder.startText.setText(labels[topIndex1] + " " + topPercent1 + "%");
        holder.endText.setText(labels[topIndex2] + " " + topPercent2 + "%");

        // 제목 설정
        if (totalCookieCount == 0) {
            setColoredText(holder.title, "쿠키 개수 : ", "모르겠어요");
        } else {
            setColoredText(holder.title, "쿠키 개수 : ", labels[topIndex1]);
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateCookieLength(SurveyViewHolder holder) {
        // 쿠키 길이 데이터 계산
        int cookieLongCount = surveyList.getCookieLongCount();
        int cookieShortCount = surveyList.getCookieShortCount();
        int totalCookieLength = cookieLongCount + cookieShortCount;

        int cookieLongPercent = calculatePercentage(cookieLongCount, totalCookieLength);
        int cookieShortPercent = calculatePercentage(cookieShortCount, totalCookieLength);

        Log.d(TAG, "updateCookieLength: Long: " + cookieLongCount + ", Short: " + cookieShortCount);
        Log.d(TAG, "updateCookieLength: Total: " + totalCookieLength);

        holder.progressBar.setProgress(cookieLongPercent);
        holder.startText.setText("길어요 " + cookieLongPercent + "%");
        holder.endText.setText("짧아요 " + cookieShortPercent + "%");

        // 제목 설정
        if (totalCookieLength == 0) {
            setColoredText(holder.title, "쿠키 길이 : ", "모르겠어요");
        } else {
            String cookieLengthTitle = (cookieLongCount >= cookieShortCount) ? "길어요" : "짧아요";
            setColoredText(holder.title, "쿠키 길이 : ", cookieLengthTitle);
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateCookieImportance(SurveyViewHolder holder) {
        // 쿠키 중요도 데이터 계산
        int cookieImportantCount = surveyList.getCookieImportantCount();
        int cookieNotImportantCount = surveyList.getCookieNotImportantCount();
        int totalCookieImportance = cookieImportantCount + cookieNotImportantCount;

        int cookieImportantPercent = calculatePercentage(cookieImportantCount, totalCookieImportance);
        int cookieNotImportantPercent = calculatePercentage(cookieNotImportantCount, totalCookieImportance);

        Log.d(TAG, "updateCookieImportance: Important: " + cookieImportantCount + ", Not Important: " + cookieNotImportantCount);
        Log.d(TAG, "updateCookieImportance: Total: " + totalCookieImportance);


        holder.progressBar.setProgress(cookieImportantPercent);
        holder.startText.setText("중요해요 " + cookieImportantPercent + "%");
        holder.endText.setText("중요하지 않아요 " + cookieNotImportantPercent + "%");

        // 제목 설정
        if (totalCookieImportance == 0) {
            setColoredText(holder.title, "쿠키 중요도 : ", "모르겠어요");
        } else {
            String cookieImportanceTitle = (cookieImportantCount >= cookieNotImportantCount) ? "중요해요" : "중요하지 않아요";
            setColoredText(holder.title, "쿠키 중요도 : ", cookieImportanceTitle);
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
        Log.d(TAG, "findTopTwoIndexes: Max1: " + max1 + " (Index: " + index1 + "), Max2: " + max2 + " (Index: " + index2 + ")");

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
