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
import com.ssuclass.cookietime.network.service.KOBISBoxOfficeService;

import java.text.SimpleDateFormat;
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void fetchBoxOfficeData() {
        String formattedDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        KOBISBoxOfficeService kobisBoxOfficeService = MovieAPI.getKOBISBoxOfficeInstance().create(KOBISBoxOfficeService.class);
        Call<KOBISBoxOfficeResponse> boxOfficeCall = kobisBoxOfficeService.getBoxOffice(getString(R.string.KOBIS_api_key), formattedDate);

        boxOfficeCall.enqueue(new Callback<KOBISBoxOfficeResponse>() {
            @Override
            public void onResponse(Call<KOBISBoxOfficeResponse> call, Response<KOBISBoxOfficeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("HomeFragment", "Box Office Data: " + response.body());
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
}