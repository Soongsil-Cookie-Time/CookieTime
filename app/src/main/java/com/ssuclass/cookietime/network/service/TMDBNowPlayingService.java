package com.ssuclass.cookietime.network.service;


import com.ssuclass.cookietime.network.response.TMDBNowPlayingResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TMDBNowPlayingService {

    @GET("movie/now_playing")
    Call<TMDBNowPlayingResponse> getNowPlayingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("region") String region,
            @Query("page") int page
    );
}
