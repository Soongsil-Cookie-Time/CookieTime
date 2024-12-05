package com.ssuclass.cookietime.presentation.survey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.databinding.FragmentSurveyBinding;

public class SurveyFragment extends Fragment {


    private static final String ARG_MOVIE_ID = "movie_id"; // Argument Key
    private FragmentSurveyBinding binding; // 뷰 바인딩 객체
    private View selectedCountButton = null; // 현재 선택된 버튼을 저장
    private View selectedImportantceButton = null; // 현재 선택된 버튼을 저장
    private View selectedLengthButton = null; // 현재 선택된 버튼을 저장


    /**
     * Fragment 생성 메서드: movieId를 Argument로 전달
     */
    public static SurveyFragment newInstance(int movieId) {
        SurveyFragment fragment = new SurveyFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setUpButtonListeners() {
        // 각 버튼에 클릭 리스너 추가
        binding.zeroButton.setOnClickListener(this::onCountButtonClicked);
        binding.oneButton.setOnClickListener(this::onCountButtonClicked);
        binding.twoButton.setOnClickListener(this::onCountButtonClicked);
        binding.threeButton.setOnClickListener(this::onCountButtonClicked);

        binding.longButton.setOnClickListener(this::onLengthButtonClicked);
        binding.shortButton.setOnClickListener(this::onLengthButtonClicked);

        binding.importantButton.setOnClickListener(this::onImportantButtonClicked);
        binding.notImportantButton.setOnClickListener(this::onImportantButtonClicked);
        binding.cookieSurveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: 화면 전환 구현
            }
        });
    }

    private void onCountButtonClicked(View clickedButton) {
        // 선택된 버튼의 상태를 초기화
        if (selectedCountButton != null) {
            resetButtonState(selectedCountButton);
        }

        // 새로운 버튼 선택
        selectedCountButton = clickedButton;
        setSelectedButtonState(clickedButton);
    }

    private void onLengthButtonClicked(View clickedButton) {
        // 선택된 버튼의 상태를 초기화
        if (selectedLengthButton != null) {
            resetButtonState(selectedLengthButton);
        }

        // 새로운 버튼 선택
        selectedLengthButton = clickedButton;
        setSelectedButtonState(clickedButton);
    }

    private void onImportantButtonClicked(View clickedButton) {
        // 선택된 버튼의 상태를 초기화
        if (selectedImportantceButton != null) {
            resetButtonState(selectedImportantceButton);
        }

        // 새로운 버튼 선택
        selectedImportantceButton = clickedButton;
        setSelectedButtonState(clickedButton);
    }

    private void resetButtonState(View button) {
        // 배경을 기본(gray)으로 설정
        button.setBackgroundTintList(null);
        button.setBackgroundResource(R.drawable.gray_button);
    }

    private void setSelectedButtonState(View button) {
        // 배경을 선택된(green)으로 설정
        button.setBackgroundTintList(null);
        button.setBackgroundResource(R.drawable.green_button);
    }

    private void resetAllButtonsBackground() {
        // Count 버튼들
        resetButtonBackground(binding.zeroButton);
        resetButtonBackground(binding.oneButton);
        resetButtonBackground(binding.twoButton);
        resetButtonBackground(binding.threeButton);

        // Length 버튼들
        resetButtonBackground(binding.longButton);
        resetButtonBackground(binding.shortButton);

        // Importance 버튼들
        resetButtonBackground(binding.importantButton);
        resetButtonBackground(binding.notImportantButton);
    }

    private void resetButtonBackground(View button) {
        button.setBackgroundResource(R.drawable.gray_button); // 기본 배경
        button.setBackgroundTintList(null); // Tint 제거
    }


    private void setAdapter() {
//        binding.keywordRecyclerView.setAdapter();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSurveyBinding.inflate(inflater, container, false);
        setUpButtonListeners();
        resetAllButtonsBackground();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 메모리 누수를 방지하기 위해 뷰 바인딩 해제
        binding = null;
    }
}