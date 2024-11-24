package com.ssuclass.cookietime.presentation.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.databinding.FragmentHomeBinding;
import com.ssuclass.cookietime.network.MovieAPI;
import com.ssuclass.cookietime.network.response.KOBISBoxOfficeResponse;
import com.ssuclass.cookietime.network.response.TMDBMovieDetailResponse;
import com.ssuclass.cookietime.network.response.TMDBMovieSearchResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnCookieButtonClickListener {

    private FragmentHomeBinding binding;
    private List<KOBISBoxOfficeResponse.DailyBoxOffice> dataList = new ArrayList<>(); // 초기화
    private CarouselAdapter adapter; // 어댑터를 멤버 변수로 정의

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 바인딩 객체 초기화
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        @SuppressLint("SimpleDateFormat") String formattedDate = new SimpleDateFormat("yyyy.MM.dd").format(calendar.getTime());
        binding.boxofficeDateText.setText(formattedDate + " 실시간 박스오피스 기준 순위");
        // RecyclerView 설정
        setupCarouselRecyclerView();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // API 호출하여 데이터 로드
        fetchBoxOfficeData();
    }

    /**
     * RecyclerView 설정 메서드
     */
    private void setupCarouselRecyclerView() {
        if (binding != null) {
            RecyclerView recyclerView = binding.boxofficeRecyclerView;

            // 어댑터 초기화
            adapter = new CarouselAdapter(dataList, this);

            // RecyclerView 설정
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);
        } else {
            Log.e("HomeFragment", "Binding is null in setupCarouselRecyclerView");
        }
    }

    /**
     * API를 호출하여 데이터를 가져오는 메서드
     */
    private void fetchBoxOfficeData() {
        // KOBIS API에서 박스오피스 데이터를 가져오는 Retrofit 호출
        MovieAPI.fetchBoxOfficeData(getString(R.string.KOBIS_api_key), new Callback<KOBISBoxOfficeResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<KOBISBoxOfficeResponse> call, @NonNull Response<KOBISBoxOfficeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 성공적으로 데이터를 받아온 경우
                    List<KOBISBoxOfficeResponse.DailyBoxOffice> newData = response.body().getBoxOfficeResult().getDailyBoxOfficeList();
                    if (newData != null && !newData.isEmpty()) {
                        dataList.clear(); // 기존 데이터를 제거
                        dataList.addAll(newData); // 새 데이터 추가
                        adapter.notifyDataSetChanged(); // RecyclerView에 변경사항 알림
                    } else {
                        Log.w("HomeFragment", "Empty box office data");
                    }
                } else {
                    // 응답이 실패한 경우 로그 출력
                    Log.e("HomeFragment", "API Response Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<KOBISBoxOfficeResponse> call, @NonNull Throwable t) {
                // API 호출 실패 로그 출력
                Log.e("HomeFragment", "Box Office API Call Failed: " + t.getMessage());
            }
        });
    }

    /**
     * TMDB API를 호출하여 영화 상세 데이터를 가져오는 메서드
     */
    private void fetchMovieDetail(int movieId) {
        MovieAPI.fetchMovieDetail(movieId, getString(R.string.TMDB_api_key), "ko-KR", new Callback<TMDBMovieDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<TMDBMovieDetailResponse> call, @NonNull Response<TMDBMovieDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TMDBMovieDetailResponse movieDetail = response.body();
                    // 영화 상세 정보 처리
                    Log.d("HomeFragment", "Fetched Movie Detail: " + movieDetail.getTitle());
                } else {
                    Log.e("HomeFragment", "Failed to fetch movie details: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TMDBMovieDetailResponse> call, @NonNull Throwable t) {
                Log.e("HomeFragment", "Movie Detail API Call Failed: " + t.getMessage());
            }
        });
    }

    /**
     * TMDB API를 호출하여 영화 검색 결과를 가져오는 메서드
     */
    private void searchMovies(String query) {
        MovieAPI.searchMovies(query, getString(R.string.TMDB_api_key), "ko-KR", "KR", new Callback<TMDBMovieSearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<TMDBMovieSearchResponse> call, @NonNull Response<TMDBMovieSearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TMDBMovieSearchResponse.Movie> movies = response.body().getResults();
                    for (TMDBMovieSearchResponse.Movie movie : movies) {
                        Log.d("HomeFragment", "Found Movie: " + movie.getTitle());
                    }
                } else {
                    Log.e("HomeFragment", "Failed to search movies: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TMDBMovieSearchResponse> call, @NonNull Throwable t) {
                Log.e("HomeFragment", "Search Movies API Call Failed: " + t.getMessage());
            }
        });
    }

    @Override
    public void onCookieButtonClick(KOBISBoxOfficeResponse.DailyBoxOffice dataModel) {
        // 박스오피스 데이터 클릭 시 영화 상세 정보 가져오기
        int movieId = Integer.parseInt(dataModel.getMovieCd()); // KOBIS의 movieCd를 TMDB의 movieId로 변환 필요
        fetchMovieDetail(movieId);
    }
}
