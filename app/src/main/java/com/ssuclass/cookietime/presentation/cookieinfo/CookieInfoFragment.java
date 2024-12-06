package com.ssuclass.cookietime.presentation.cookieinfo;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.databinding.FragmentCookieInfoBinding;
import com.ssuclass.cookietime.domain.CookieKeywordModel;
import com.ssuclass.cookietime.domain.SurveyProgressModel;
import com.ssuclass.cookietime.network.MovieAPI;
import com.ssuclass.cookietime.network.response.TMDBMovieDetailResponse;
import com.ssuclass.cookietime.presentation.badgemanager.InstagramSharingActivity;
import com.ssuclass.cookietime.presentation.community.posts.PostsActivity;
import com.ssuclass.cookietime.presentation.survey.SurveyFragment;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CookieInfoFragment extends Fragment {

    private static final String ARG_MOVIE_ID = "movie_id"; // Argument Key
    private static final String ARG_MOVIE_TITLE = "movie_title"; // Argument Key for title
    private SurveyProgressAdapter surveyAdapter;
    private KeywordAdapter keywordAdapter;
    private FragmentCookieInfoBinding binding; // 뷰 바인딩 객체
    private FirebaseFirestore db;
    private String posterPath;

    public static CookieInfoFragment newInstance(int movieId, String movieTitle) {
        CookieInfoFragment fragment = new CookieInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movieId);
        args.putString(ARG_MOVIE_TITLE, movieTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            int movieId = getArguments().getInt(ARG_MOVIE_ID, -1);
            String movieTitle = getArguments().getString(ARG_MOVIE_TITLE);
            updateButtonState(movieId, movieTitle);
        }
    }

    /**
     * 설문 버튼 상태를 업데이트하는 메서드
     */
    private void updateButtonState(int movieId, String movieTitle) {
        checkIfWatchedMovie(movieId, isWatched -> {
            if (isWatched) {
                binding.goToCookieCommunityButton.setEnabled(true);
                binding.goToCookieCommunityButton.setText("커뮤니티 입장");
                binding.goToCookieCommunityButton.setOnClickListener(view -> {
                    checkIfCommunityExits(movieId, exists -> {
                        if (exists) {
                            Map<String, Object> communityData = new HashMap<>();
                            communityData.put("title", movieTitle);
                            communityData.put("poster_path", posterPath); // FIXME: 12/6/24 poster_path 추가

                            db.collection("Communities")
                                    .document(Integer.toString(movieId))
                                    .set(communityData)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Firestore", "커뮤니티가 성공적으로 생성되었습니다.");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Firestore", "커뮤니티 생성 실패: ", e);
                                    });

                            Intent intent = new Intent(getContext(), PostsActivity.class);
                            intent.putExtra("movieId", Integer.toString(movieId));
                            intent.putExtra("movieTitle", movieTitle);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getContext(), PostsActivity.class);
                            intent.putExtra("movie_id", Integer.toString(movieId)); // FIXME: 12/6/24 movie_id 단일화
                            startActivity(intent);
                        }
                    });
                });
                binding.cookieInfoSurveyButton.setText("오늘의 쿠키 스토리 공유하기");
                binding.cookieInfoSurveyButton.setOnClickListener(view -> {
                    //TODO: 쿠키 스토리 공유 화면으로 전환
                    Intent intent = new Intent(getContext(), InstagramSharingActivity.class);
                    intent.putExtra("movie_id", Integer.toString(movieId)); // FIXME: 2024. 12. 6. 영화 아이디값 단일화
                    startActivity(intent);
                });
            } else {
                binding.cookieInfoSurveyButton.setEnabled(true);
                binding.goToCookieCommunityButton.setText("설문하여 입장");
                binding.goToCookieCommunityButton.setEnabled(false);
                binding.cookieInfoSurveyButton.setOnClickListener(view -> {
                    SurveyFragment surveyFragment = SurveyFragment.newInstance(movieId, movieTitle);
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, surveyFragment)
                            .addToBackStack(null)
                            .commit();
                });
            }
        });
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 뷰 바인딩 초기화
        binding = FragmentCookieInfoBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();

        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }

        // RecyclerView 초기화
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
        binding = null;
    }

    /**
     * 사용자가 이미 본 영화인지 확인하는 메서드
     */
    private void checkIfWatchedMovie(int movieId, OnCheckMovieCallback callback) {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) {
            System.err.println("User ID is null. Cannot save watched movie.");
            return;
        }
        db.collection("User")
                .document(userId)
                .collection("WatchedMovie")
                .document(String.valueOf(movieId)) // Movie ID를 Document ID로 사용
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        callback.onCheckResult(task.getResult().exists());
                    } else {
                        Log.e("Firestore", "Error checking watched movie", task.getException());
                        callback.onCheckResult(false);
                    }
                });
    }

    private void checkIfCommunityExits(int movieId, OnCheckMovieCommunityCallback callback) {
        db.collection("Community")
                .document(String.valueOf(movieId)) // Movie ID를 Document ID로 사용
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        callback.onCheckResult(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onCheckResult(false);
                    }
                });
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
                    posterPath = dataModel.getPosterPath();
                    updateUIWithMovieDetail(dataModel); // UI 업데이트
                    // 해당 도큐먼트가 없는 경우에만 신규 도큐먼트 생성
                    checkMovieDocument(movieId, exists -> {
                        if (!exists) {
                            addMovieDocumentWithId(movieId);
                        }
                    });
                } else {
                    try {
                        String errorBody = response.errorBody().string();
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

    private void addMovieDocumentWithId(Integer movieId) {
        // 쿠키정보 데이터베이스 초깃값 설정
        db.collection("Cookie")
                .document(movieId.toString())
                .set(new SurveyProgressModel(0, 0, 0, 0, 0, 0, 0, 0))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println("Document successfully added under movieId: " + movieId);
                    } else {
                        System.err.println("Error adding document: " + task.getException());
                    }
                });

        // 하위 컬렉션 참조 생성
        CollectionReference subCollectionRef = db.collection("Cookie").document(movieId.toString()).collection("Keyword");

        // 데이터 모델 및 ID 매핑
        Map<String, CookieKeywordModel> dataModels = new HashMap<>();
        dataModels.put("cookieWaitLong", new CookieKeywordModel("엔딩 크레딧 이후 오래 기다렸어요", 0, false));
        dataModels.put("cookieWorthWatching", new CookieKeywordModel("보길 잘했어요", 0, true));
        dataModels.put("cookieNextSeries", new CookieKeywordModel("다음 시리즈 떡밥이 포함되어 있어요", 0, true));
        dataModels.put("cookieRegret", new CookieKeywordModel("괜히 봤어요", 0, false));
        dataModels.put("cookieQuickExit", new CookieKeywordModel("엔딩 크레딧 직후 바로 나왔어요", 0, true));
        dataModels.put("cookieContentFun", new CookieKeywordModel("쿠키 내용이 재밌어요", 0, true));
        dataModels.put("cookieEasterEgg", new CookieKeywordModel("이스터에그가 포함되어 있어요", 0, true));

        // 각각의 데이터를 지정된 ID로 하위 컬렉션에 추가
        for (Map.Entry<String, CookieKeywordModel> entry : dataModels.entrySet()) {
            String documentId = entry.getKey(); // 미리 지정한 ID
            CookieKeywordModel dataModel = entry.getValue();

            subCollectionRef.document(documentId)
                    .set(dataModel)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            System.out.println("Document successfully added with ID: " + documentId);
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
        int hours = runtime / 60;
        int minutes = runtime % 60;
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


    interface OnCheckMovieCallback {
        void onCheckResult(boolean exists);
    }

    interface OnCheckMovieCommunityCallback {
        void onCheckResult(boolean exists);
    }
}
