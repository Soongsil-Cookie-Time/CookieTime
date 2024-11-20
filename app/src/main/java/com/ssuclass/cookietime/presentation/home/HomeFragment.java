package com.ssuclass.cookietime.presentation.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.network.MovieAPI;
import com.ssuclass.cookietime.network.response.KOBISBoxOfficeResponse;
import com.ssuclass.cookietime.network.response.KOBISSearchResponse;
import com.ssuclass.cookietime.network.service.KOBISBoxOfficeService;
import com.ssuclass.cookietime.network.service.KOBISSearchService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fetchBoxOfficeData();
        fetchMovieData("청설");
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void fetchBoxOfficeData() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1); // 하루를 뺌
        Date yesterday = calendar.getTime();

        // 어제 날짜를 yyyyMMdd 형식으로 변환
        String formattedDate = new SimpleDateFormat("yyyyMMdd").format(yesterday);

        KOBISBoxOfficeService kobisBoxOfficeService = MovieAPI.getKOBISBoxOfficeInstance().create(KOBISBoxOfficeService.class);
        Call<KOBISBoxOfficeResponse> boxOfficeCall = kobisBoxOfficeService.getBoxOffice(getString(R.string.KOBIS_api_key), formattedDate);

        boxOfficeCall.enqueue(new Callback<KOBISBoxOfficeResponse>() {
            @Override
            public void onResponse(Call<KOBISBoxOfficeResponse> call, Response<KOBISBoxOfficeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("HomeFragment", "Box Office Data: " + response.body().getBoxOfficeResult().getDailyBoxOfficeList().toString());
                } else {
                    Log.e("HomeFragment", "API Response Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<KOBISBoxOfficeResponse> call, Throwable t) {
                Log.e("HomeFragment", "Box Office API Call Failed: " + t.getMessage());
            }
        });
    }
    private void fetchMovieData(String movieName) {
        KOBISSearchService kobisSearchService = MovieAPI.getKOBISSearchInstance().create(KOBISSearchService.class);

        // Retrofit API 호출
        Call<KOBISSearchResponse> searchCall = kobisSearchService.getMovies(getString(R.string.KOBIS_api_key), movieName);

        searchCall.enqueue(new Callback<KOBISSearchResponse>() {
            @Override
            public void onResponse(Call<KOBISSearchResponse> call, Response<KOBISSearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("HomeFragment", "Search Results: " + response.body().getMovieListResult().toString());
                } else {
                    Log.e("HomeFragment", "API Response Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<KOBISSearchResponse> call, Throwable t) {
                Log.e("HomeFragment", "Movie Search API Call Failed: " + t.getMessage());
            }
        });
    }


}