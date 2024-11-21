package com.ssuclass.cookietime.network.service;

import com.ssuclass.cookietime.network.response.KOBISSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface KOBISSearchService {
    @GET("searchMovieList.json")
    Call<KOBISSearchResponse> getMovies(
            @Query("key") String apiKey,       // API 키
            @Query("movieNm") String movieName // 영화 이름
    );
}