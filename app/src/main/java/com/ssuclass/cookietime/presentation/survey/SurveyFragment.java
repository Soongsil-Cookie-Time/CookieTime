package com.ssuclass.cookietime.presentation.survey;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.databinding.FragmentCookieInfoBinding;
import com.ssuclass.cookietime.databinding.FragmentSurveyBinding;
import com.ssuclass.cookietime.presentation.cookieinfo.CookieInfoFragment;

public class SurveyFragment extends Fragment {


    private static final String ARG_MOVIE_ID = "movie_id"; // Argument Key
    private FragmentSurveyBinding binding; // 뷰 바인딩 객체

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSurveyBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
}