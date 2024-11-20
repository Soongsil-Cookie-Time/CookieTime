package com.ssuclass.cookietime.network.service;

import com.ssuclass.cookietime.network.response.KOBISBoxOfficeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface KOBISBoxOfficeService {
    @GET("searchMovieList.json")
    Call<KOBISBoxOfficeResponse> getMovies(
            @Query("key") String apiKey,
            @Query("targetDt") String targetDt
    );
}