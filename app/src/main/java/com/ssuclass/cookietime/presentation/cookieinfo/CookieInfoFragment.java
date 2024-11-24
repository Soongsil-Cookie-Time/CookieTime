package com.ssuclass.cookietime.presentation.cookieinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ssuclass.cookietime.databinding.FragmentCookieInfoBinding;
import com.ssuclass.cookietime.domain.CookieKeywordModel;
import com.ssuclass.cookietime.domain.SurveyProgressModel;

import java.util.ArrayList;
import java.util.List;

public class CookieInfoFragment extends Fragment {

    private FragmentCookieInfoBinding binding; // 뷰 바인딩 객체

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 뷰 바인딩 초기화
        binding = FragmentCookieInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // RecyclerView 설정
        setKeywordRecyclerView();
        setSurveyRecyclerView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 메모리 누수를 방지하기 위해 뷰 바인딩 해제
        binding = null;
    }

    /**
     * 키워드 RecyclerView 설정
     */
    private void setKeywordRecyclerView() {
        binding.keywordRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 예시 데이터
        List<CookieKeywordModel> keywordList = new ArrayList<>();

        keywordList.add(new CookieKeywordModel("감동적인 엔딩", 45));
        keywordList.add(new CookieKeywordModel("지루하지 않은 전개", 30));
        keywordList.add(new CookieKeywordModel("예상치 못한 반전", 25));
        keywordList.add(new CookieKeywordModel("캐릭터 매력적", 20));
        keywordList.add(new CookieKeywordModel("완벽한 음악", 15));
        keywordList.add(new CookieKeywordModel("압도적인 비주얼", 10));
        keywordList.add(new CookieKeywordModel("다소 긴 러닝타임", 5));

        // 어댑터 설정
        KeywordAdapter keywordAdapter = new KeywordAdapter(keywordList);
        binding.keywordRecyclerView.setAdapter(keywordAdapter);
    }

    /**
     * 설문 RecyclerView 설정
     */
    private void setSurveyRecyclerView() {
        binding.cookieSurveyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 예시 데이터
        SurveyProgressModel surveyProgressModel = new SurveyProgressModel();
        surveyProgressModel.setCookieZeroCount(5);
        surveyProgressModel.setCookieOneCount(15);
        surveyProgressModel.setCookieTwoCount(25);
        surveyProgressModel.setCookieThreeCount(10);

        // 쿠키 길이 데이터
        surveyProgressModel.setCookieLongCount(30);
        surveyProgressModel.setCookieShortCount(20);

        // 쿠키 중요도 데이터
        surveyProgressModel.setCookieImportantCount(35);
        surveyProgressModel.setCookieNotImportantCount(15);


        // 어댑터 설정
        SurveyProgressAdapter surveyAdapter = new SurveyProgressAdapter(surveyProgressModel);
        binding.cookieSurveyRecyclerView.setAdapter(surveyAdapter);
    }
}
