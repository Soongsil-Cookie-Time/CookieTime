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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.firebase.firestore.FirebaseFirestore;

public class CookieInfoFragment extends Fragment {

    private SurveyProgressAdapter surveyAdapter;
    private KeywordAdapter keywordAdapter;
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

        // RecyclerView 초기화 시 어댑터를 클래스 필드에 설정
        keywordAdapter = new KeywordAdapter(new ArrayList<>());
        binding.keywordRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.keywordRecyclerView.setAdapter(keywordAdapter);

        surveyAdapter = new SurveyProgressAdapter(new SurveyProgressModel(0, 0, 0, 0, 0, 0, 0, 0));
        binding.cookieSurveyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.cookieSurveyRecyclerView.setAdapter(surveyAdapter);

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
                    checkMovieDocument(movieId, exists -> {
                        if (!exists) {
                            addMovieDocumentWithId(movieId);
                        }
                    });
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

    private void setAdapterWithCookieData(List<CookieKeywordModel> cookieKeywordModelArray, SurveyProgressModel surveyProgressModel) {
        keywordAdapter.updateKeywords(cookieKeywordModelArray);
        surveyAdapter.updateSurveyData(surveyProgressModel);
    }


    private void checkMovieDocument(Integer movieId, OnCheckMovieCallback callback) {
        db.collection("Cookie")
                .document(movieId.toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        SurveyProgressModel surveyProgressModel = task.getResult().toObject(SurveyProgressModel.class);
                        if (document.exists()) {
                            // 하위 컬렉션 데이터 가져오기
                            db.collection("Cookie")
                                    .document(movieId.toString())
                                    .collection("Keyword")
                                    .get()
                                    .addOnCompleteListener(subTask -> {
                                        if (subTask.isSuccessful() && subTask.getResult() != null) {
                                            // 하위 컬렉션 데이터를 원본 클래스 형태로 변환
                                            List<CookieKeywordModel> keywordList = subTask.getResult().toObjects(CookieKeywordModel.class);
                                            // 어댑터 설정 및 콜백 호출
                                            setAdapterWithCookieData(keywordList, surveyProgressModel);
                                            callback.onCheckResult(true);
                                        } else {
                                            Log.e("Firestore", "Error fetching subcollection", subTask.getException());
                                            callback.onCheckResult(false);
                                        }
                                    });
                        } else {
                            callback.onCheckResult(false); // 문서가 존재하지 않을 경우
                        }
                    } else {
                        Log.e("Firestore", "Error checking document", task.getException());
                        callback.onCheckResult(false);
                    }
                });
    }

    interface OnCheckMovieCallback {
        void onCheckResult(boolean exists);
    }

    private void addMovieDocumentWithId(Integer movieId) {
        // 쿠키정보 데이터베이스 초깃값 설정
        db.collection("Cookie")
            .document(movieId.toString())
            .set(new SurveyProgressModel(0,0,0,0,0,0,0,0))
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    System.out.println("Document successfully added under movieId: " + movieId);
                } else {
                    System.err.println("Error adding document: " + task.getException());
                }
            });
        // 하위 컬렉션 참조 생성
        CollectionReference subCollectionRef = db.collection("Cookie").document(movieId.toString()).collection("Keyword");

        // 여러 데이터 모델 생성
        List<CookieKeywordModel> dataModels = Arrays.asList(
            new CookieKeywordModel("엔딩 크레딧 이후 오래 기다렸어요", 0, false),
            new CookieKeywordModel("보길 잘했어요", 0, true),
            new CookieKeywordModel("다음 시리즈 떡밥이 포함되어 있어요", 0, true),
            new CookieKeywordModel("엔딩 크레딧 이후 오래 기다렸어요", 0, false),
            new CookieKeywordModel("괜히 봤어요", 0, false),
            new CookieKeywordModel("엔딩 크레딧 직후 바로 나왔어요", 0, true),
            new CookieKeywordModel("쿠키 내용이 재밌어요", 0, true),
            new CookieKeywordModel("이스터에그가 포함되어 있어요", 0, true)
        );

        // 각각의 데이터를 하위 컬렉션에 추가
        for (CookieKeywordModel dataModel : dataModels) {
            subCollectionRef.add(dataModel)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    System.out.println("Document successfully added under movieId: " + movieId);
                } else {
                    System.err.println("Error adding document: " + task.getException());
                }
            });
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

}
