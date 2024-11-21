package com.ssuclass.cookietime.network.service;

import com.ssuclass.cookietime.network.response.KOBISBoxOfficeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface KOBISBoxOfficeService {
    @GET("searchDailyBoxOfficeList.json")
    Call<KOBISBoxOfficeResponse> getBoxOffice(
            @Query("key") String apiKey,
            @Query("targetDt") String targetDate // YYYYMMDD format
    );
}
