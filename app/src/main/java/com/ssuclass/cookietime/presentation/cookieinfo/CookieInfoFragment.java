package com.ssuclass.cookietime.presentation.cookieinfo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.databinding.FragmentCookieInfoBinding;
import com.ssuclass.cookietime.domain.CookieKeywordModel;
import com.ssuclass.cookietime.domain.SurveyProgressModel;
import com.ssuclass.cookietime.network.MovieAPI;
import com.ssuclass.cookietime.network.response.TMDBMovieDetailResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CookieInfoFragment extends Fragment {

    private static final String ARG_MOVIE_ID = "movie_id"; // Argument Key
    private FragmentCookieInfoBinding binding; // 뷰 바인딩 객체

    /**
     * Fragment 생성 메서드: movieId를 Argument로 전달
     */
    public static CookieInfoFragment newInstance(int movieId) {
        CookieInfoFragment fragment = new CookieInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 뷰 바인딩 초기화
        binding = FragmentCookieInfoBinding.inflate(inflater, container, false);

        // Argument에서 movieId 가져오기
        if (getArguments() != null) {
            int movieId = getArguments().getInt(ARG_MOVIE_ID, -1);
            fetchMovieDetail(movieId); // API 호출
        }

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
     * TMDB API를 호출하여 영화 세부 정보를 가져오는 메서드
     */
    private void fetchMovieDetail(int movieId) {
        MovieAPI.fetchMovieDetail(movieId, getString(R.string.TMDB_api_key), "ko-KR", new Callback<TMDBMovieDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<TMDBMovieDetailResponse> call, @NonNull Response<TMDBMovieDetailResponse> response) {
                Log.d("Response", String.valueOf(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    TMDBMovieDetailResponse dataModel = response.body();
                    updateUIWithMovieDetail(dataModel); // UI 업데이트
                } else {
                    try {
                        String errorBody = response.errorBody().string(); // 에러 메시지 읽기
                        Log.e("API Response Error", errorBody);
                    } catch (IOException e) {
                        Log.e("API Response Error", "Failed to read error body", e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TMDBMovieDetailResponse> call, @NonNull Throwable t) {
                Log.e("CookieInfoFragment", "API Call Failed: " + t.getMessage());
            }
        });
    }

    /**
     * 영화 상세 정보로 UI 업데이트
     */
    @SuppressLint("SetTextI18n")
    private void updateUIWithMovieDetail(TMDBMovieDetailResponse dataModel) {
        if (binding != null) {
            Glide.with(binding.cookieInfoPosterImageView).load("https://image.tmdb.org/t/p/w500" + dataModel.getPosterPath()).into(binding.cookieInfoPosterImageView);
            // 러닝타임 및 장르 포맷 변경
            String runtimeAndGenre = formatRuntimeAndGenre(dataModel.getRuntime(), dataModel.getGenres());
            binding.runningTime.setText(runtimeAndGenre);

            if (dataModel.getProductionCompanies() != null && !dataModel.getProductionCompanies().isEmpty()) {
                binding.cookieInfoCompany.setText(dataModel.getProductionCompanies().get(0).getName());
            } else {
                binding.cookieInfoCompany.setText("제작사 정보 없음");
            }
            binding.cookieInfoPageTitleText.setText(dataModel.getTitle());
            String releaseDate = formatReleaseDate(dataModel.getReleaseDate());
            binding.cookieInfoReleaseDate.setText(releaseDate);
            binding.goToCookieCommunityText.setText("[" + dataModel.getTitle() + "] 커뮤니티 입장");
        }
    }

    /**
     * 러닝타임과 첫 번째 장르를 포맷팅하여 반환하는 메서드
     */
    private String formatRuntimeAndGenre(int runtime, List<TMDBMovieDetailResponse.Genre> genres) {
        // 러닝타임을 "시간 분" 형식으로 변환
        int hours = runtime / 60;
        int minutes = runtime % 60;

        // 첫 번째 장르 가져오기
        String genreName = (genres != null && !genres.isEmpty()) ? genres.get(0).getName() : "장르 정보 없음";

        return hours + "시간 " + minutes + "분 | " + genreName;
    }


    /**
     * 날짜 형식을 "YYYY.MM.DD 개봉"으로 변환하는 메서드
     */
    private String formatReleaseDate(String releaseDate) {
        if (releaseDate == null || releaseDate.isEmpty()) return "개봉일 정보 없음";

        try {
            // 기존 날짜 형식 (YYYY-MM-DD)
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = originalFormat.parse(releaseDate);

            // 원하는 날짜 형식 (YYYY.MM.DD 개봉)
            SimpleDateFormat desiredFormat = new SimpleDateFormat("yyyy.MM.dd");
            return desiredFormat.format(date) + " 개봉";
        } catch (ParseException e) {
            e.printStackTrace();
            return "날짜 형식 오류";
        }
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
