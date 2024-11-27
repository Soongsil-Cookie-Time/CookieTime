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
import com.google.firebase.firestore.DocumentSnapshot;
import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.databinding.FragmentCookieInfoBinding;
import com.ssuclass.cookietime.domain.CookieKeywordModel;
import com.ssuclass.cookietime.domain.MovieDetailModel;
import com.ssuclass.cookietime.domain.SurveyProgressModel;
import com.ssuclass.cookietime.network.MovieAPI;
import com.ssuclass.cookietime.network.response.TMDBMovieDetailResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.firebase.firestore.FirebaseFirestore;

public class CookieInfoFragment extends Fragment {

    private static final String ARG_MOVIE_ID = "movie_id"; // Argument Key
    private FragmentCookieInfoBinding binding; // 뷰 바인딩 객체
    private FirebaseFirestore db;

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
        db = FirebaseFirestore.getInstance();
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
                    // 해당 도큐먼트가 없는 경우에만 신규 도큐먼트 생성
                    if (!checkMovieDocument(dataModel.getId())) {
                        addMovieDocumentWithId(movieId, dataModel);
                    }
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

    private void updateUIWithSurveyData(MovieDetailModel dataModel) {
        setKeywordRecyclerView(dataModel.getCookieKeywordCountArray());
        setSurveyRecyclerView(dataModel.getSurveyProgressModel());
    }

    private Boolean checkMovieDocument(Integer movieId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        db.collection("Movie")
                .document(movieId.toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Firestore 데이터를 MovieDetailModel 객체로 변환
                            MovieDetailModel movie = document.toObject(MovieDetailModel.class);
                            future.complete(true); // 도큐먼트가 존재하면 true
                            if (movie != null) {
                                updateUIWithSurveyData(movie);
                            }
                        } else {
                            Log.d("Firestore", "No such document!");
                            future.complete(false); // 도큐먼트가 존재하지 않으면 false
                        }
                    } else {
                        Log.e("Firestore", "Error getting document", task.getException());
                        future.complete(false); // 작업 실패 시 false
                    }
                });
        try {
            return future.get(); // 결과를 기다림
        } catch (Exception e) {
            return false; // 예외 발생 시 false 반환
        }
    }



    private void addMovieDocumentWithId(Integer movieId, TMDBMovieDetailResponse dataModel) {
        db.collection("Movie")
                .document(movieId.toString())
                .set(dataModel)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Document 추가 성공
                        System.out.println("Document successfully created with ID: " + movieId);
                    } else {
                        // Document 추가 실패
                        System.err.println("Error creating document: " + task.getException());
                    }
                });
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
    private void setKeywordRecyclerView(List<CookieKeywordModel> keywordList) {
        binding.keywordRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // 어댑터 설정
        KeywordAdapter keywordAdapter = new KeywordAdapter(keywordList);
        binding.keywordRecyclerView.setAdapter(keywordAdapter);
    }

    /**
     * 설문 RecyclerView 설정
     */
    private void setSurveyRecyclerView(SurveyProgressModel surveyProgressModel) {
        binding.cookieSurveyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // 어댑터 설정
        SurveyProgressAdapter surveyAdapter = new SurveyProgressAdapter(surveyProgressModel);
        binding.cookieSurveyRecyclerView.setAdapter(surveyAdapter);
    }
}
