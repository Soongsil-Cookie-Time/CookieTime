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
import androidx.appcompat.widget.SearchView;

public class HomeFragment extends Fragment implements OnCookieButtonClickListener {

    private FragmentHomeBinding binding;
    private List<KOBISBoxOfficeResponse.DailyBoxOffice> dataList = new ArrayList<>();
    private CarouselAdapter adapter;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        @SuppressLint("SimpleDateFormat") String formattedDate = new SimpleDateFormat("yyyy.MM.dd").format(calendar.getTime());
        binding.boxofficeDateText.setText(formattedDate + " 실시간 박스오피스 기준 순위");

        // RecyclerView 초기화
        setupCarouselRecyclerView();

        // SearchView 초기화
        setupSearchView();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchBoxOfficeData(); // 박스오피스 데이터 로드
    }

    private void setupCarouselRecyclerView() {
        RecyclerView recyclerView = binding.boxofficeRecyclerView;
        adapter = new CarouselAdapter(dataList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    /**
     * SearchView 설정
     */
    private void setupSearchView() {
        SearchView searchView = binding.homeSearchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 검색 버튼을 눌렀을 때 처리
                if (!query.isEmpty()) {
                    searchMovies(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 사용자가 입력하는 동안 처리 (실시간 처리 필요시 사용)
                return false;
            }
        });
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
     * TMDB API를 호출하여 영화 검색 결과를 가져오는 메서드
     */
    private void searchMovies(String query) {
        MovieAPI.searchMovies(query, getString(R.string.TMDB_api_key), "ko-KR", "KR", new Callback<TMDBMovieSearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<TMDBMovieSearchResponse> call, @NonNull Response<TMDBMovieSearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TMDBMovieSearchResponse.Movie> movies = response.body().getResults();
                    if (movies != null && !movies.isEmpty()) {
                        // 검색 결과 출력 (Log 또는 RecyclerView 업데이트)
                        for (TMDBMovieSearchResponse.Movie movie : movies) {
                            Log.d("HomeFragment", "Found Movie: " + movie.getTitle());
                        }
                    } else {
                        Log.w("HomeFragment", "No movies found for query: " + query);
                    }
                } else {
                    Log.e("HomeFragment", "Search API Response Error: " + response.message());
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
        int movieId = Integer.parseInt(dataModel.getMovieCd());
        fetchMovieDetail(movieId);
    }

    private void fetchMovieDetail(int movieId) {
        MovieAPI.fetchMovieDetail(movieId, getString(R.string.TMDB_api_key), "ko-KR", new Callback<TMDBMovieDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<TMDBMovieDetailResponse> call, @NonNull Response<TMDBMovieDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TMDBMovieDetailResponse movieDetail = response.body();
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
}
