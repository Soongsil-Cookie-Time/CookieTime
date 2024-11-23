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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnCookieButtonClickListener {

    private FragmentHomeBinding binding;
    private List<BoxOfficeDataModel> dataList = new ArrayList<>(); // 초기화

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 바인딩 객체를 초기화
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // RecyclerView 초기화 설정
        setCarouselRecyclerView();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchBoxOfficeData();
    }

    private void setCarouselRecyclerView() {
        if (binding != null) { // Null Check
            RecyclerView recyclerView = binding.boxofficeRecyclerView;

            // 빈 어댑터로 초기화
            CarouselAdapter adapter = new CarouselAdapter(dataList, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        } else {
            Log.e("HomeFragment", "Binding is null in setCarouselRecyclerView");
        }
    }

    private void fetchBoxOfficeData() {
        MovieAPI.fetchBoxOfficeData(getString(R.string.KOBIS_api_key), new Callback<KOBISBoxOfficeResponse>() {
            @Override
            public void onResponse(@NonNull Call<KOBISBoxOfficeResponse> call, @NonNull Response<KOBISBoxOfficeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

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

    @Override
    public void onCookieButtonClick(BoxOfficeDataModel dataModel) {
        // TODO: 버튼 클릭 시 이벤트 구현
    }
}
