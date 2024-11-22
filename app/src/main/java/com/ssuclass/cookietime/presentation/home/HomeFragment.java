package com.ssuclass.cookietime.presentation.home;

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
import com.ssuclass.cookietime.domain.BoxOfficeDataModel;
import com.ssuclass.cookietime.network.MovieAPI;
import com.ssuclass.cookietime.network.response.KOBISBoxOfficeResponse;
import com.ssuclass.cookietime.network.response.KOBISSearchResponse;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnCookieButtonClickListener{

    private FragmentHomeBinding binding;
    List<BoxOfficeDataModel> dataList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setCarouselRecyclerView();
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        setCarouselRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchBoxOfficeData();
    }

    private void setCarouselRecyclerView(){
        RecyclerView recyclerView = binding.boxofficeRecyclerView;
        CarouselAdapter adapter = new CarouselAdapter(dataList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void fetchBoxOfficeData() {
        MovieAPI.fetchBoxOfficeData(getString(R.string.KOBIS_api_key), new Callback<KOBISBoxOfficeResponse>() {
            @Override
            public void onResponse(@NonNull Call<KOBISBoxOfficeResponse> call, @NonNull Response<KOBISBoxOfficeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("HomeFragment", "Box Office Data: " + response.body().getBoxOfficeResult().getDailyBoxOfficeList().toString());
                } else {
                    Log.e("HomeFragment", "API Response Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<KOBISBoxOfficeResponse> call, Throwable t) {
                Log.e("HomeFragment", "Box Office API Call Failed: " + t.getMessage());
            }
        });
    }

    private void fetchMovieData(String movieName) {
        MovieAPI.fetchMovieData(getString(R.string.KOBIS_api_key), movieName, new Callback<KOBISSearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<KOBISSearchResponse> call, @NonNull Response<KOBISSearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("HomeFragment", "Search Results: " + response.body().getMovieListResult().toString());
                } else {
                    Log.e("HomeFragment", "API Response Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<KOBISSearchResponse> call, @NonNull Throwable t) {
                Log.e("HomeFragment", "Movie Search API Call Failed: " + t.getMessage());
            }
        });
    }


    @Override
    public void onCookieButtonClick(BoxOfficeDataModel dataModel) {
        //TODO: 버튼 클릭 시 이벤트 구현
    }
}