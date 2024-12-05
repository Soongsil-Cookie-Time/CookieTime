package com.ssuclass.cookietime.presentation.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.databinding.FragmentHomeBinding;
import com.ssuclass.cookietime.network.MovieAPI;
import com.ssuclass.cookietime.network.response.TMDBMovieSearchResponse;
import com.ssuclass.cookietime.network.response.TMDBNowPlayingResponse;
import com.ssuclass.cookietime.presentation.cookieinfo.CookieInfoFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnCookieButtonClickListener {

    private final List<TMDBNowPlayingResponse.Movie> dataList = new ArrayList<>();
    private FragmentHomeBinding binding;
    private CarouselAdapter adapter;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") String formattedDate = new SimpleDateFormat("yyyy.MM.dd").format(calendar.getTime());
        binding.boxofficeDateText.setText(formattedDate + " 기준 현재 상영작");

        // RecyclerView 초기화
        setupCarouselRecyclerView();

        // SearchView 초기화
        setupSearchView();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchNowPlayingMovies(); // 현재 상영작 데이터 로드
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
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
     * TMDB Now Playing API를 호출하여 현재 상영 중인 영화 데이터를 가져오는 메서드
     */
    private void fetchNowPlayingMovies() {
        MovieAPI.fetchNowPlayingMovies(
                getString(R.string.TMDB_api_key), // API 키
                "ko-KR",                         // 언어
                "KR",                            // 지역
                1,                               // 페이지 번호
                new Callback<TMDBNowPlayingResponse>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(@NonNull Call<TMDBNowPlayingResponse> call, @NonNull Response<TMDBNowPlayingResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<TMDBNowPlayingResponse.Movie> movies = response.body().getResults();
                            if (movies != null && !movies.isEmpty()) {
                                // boxOfficeDetailList를 갱신하고 RecyclerView 업데이트
                                dataList.clear();
                                dataList.addAll(movies);

                                // UI 업데이트
                                Log.d("HomeFragment", "Now Playing Movies Fetched: " + movies.size());
                                adapter.notifyDataSetChanged();
                            } else {
                                Log.w("HomeFragment", "No Now Playing movies found");
                            }
                        } else {
                            Log.e("HomeFragment", "TMDB API Response Error: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TMDBNowPlayingResponse> call, @NonNull Throwable t) {
                        Log.e("HomeFragment", "TMDB Now Playing API Call Failed: " + t.getMessage());
                    }
                }
        );
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
    public void onCookieButtonClick(TMDBNowPlayingResponse.Movie dataModel) {
        // CookieInfoFragment 생성 및 Movie ID 전달
        CookieInfoFragment cookieInfoFragment = CookieInfoFragment.newInstance(dataModel.getId());

        // Fragment 전환
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, cookieInfoFragment) // Replace with the ID of your container
                .addToBackStack(null) // 뒤로 가기 지원
                .commit();
    }


}

