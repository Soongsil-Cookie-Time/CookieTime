package com.ssuclass.cookietime.network.service;

import com.ssuclass.cookietime.network.response.TMDBMovieDetailResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBMovieDetailService {
    @GET("movie/{movie_id}")
    Call<TMDBMovieDetailResponse> getMovieDetail(
            @Path("movie_id") int movieId,         // The ID of the movie
            @Query("api_key") String apiKey,       // API key for authentication
            @Query("language") String language     // Language preference, e.g., "ko-KR"
    );
}
