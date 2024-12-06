package com.ssuclass.cookietime.presentation.search;

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
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.databinding.FragmentSearchBinding;
import com.ssuclass.cookietime.network.MovieAPI;
import com.ssuclass.cookietime.network.response.TMDBMovieSearchResponse;
import com.ssuclass.cookietime.presentation.cookieinfo.CookieInfoFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private final List<TMDBMovieSearchResponse.Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FragmentSearchBinding binding;
    private SearchAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(getLayoutInflater());
        recyclerView = binding.searchResultRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SearchAdapter(movieList, new OnMovieClickListener() {
            @Override
            public void onMovieClick(int movieId, String movieTitle) {
                CookieInfoFragment fragment = CookieInfoFragment.newInstance(movieId, movieTitle);
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        recyclerView.setAdapter(adapter);
        setupSearchView();
        // 전달받은 검색 키워드로 API 호출
        if (getArguments() != null) {
            String query = getArguments().getString("query");
            if (query != null) {
                fetchSearchResults(query);
            }
        }
        return binding.getRoot();
    }

    private void setupSearchView() {
        androidx.appcompat.widget.SearchView searchView = binding.searchView;
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchSearchResults(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void fetchSearchResults(String query) {
        MovieAPI.searchMovies(query, getString(R.string.TMDB_api_key), "ko-KR", "KR", new Callback<TMDBMovieSearchResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<TMDBMovieSearchResponse> call, Response<TMDBMovieSearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieList.clear();
                    movieList.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("SearchFragment", "Search API Response Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<TMDBMovieSearchResponse> call, Throwable t) {
                Log.e("SearchFragment", "Search API Call Failed: " + t.getMessage());
            }
        });
    }
}
