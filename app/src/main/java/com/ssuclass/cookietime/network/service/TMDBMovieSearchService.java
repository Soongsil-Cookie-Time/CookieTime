package com.ssuclass.cookietime.network.service;

import com.ssuclass.cookietime.network.response.TMDBMovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TMDBMovieSearchService {
    @GET("search/movie")
    Call<TMDBMovieSearchResponse> searchMovies(
            @Query("api_key") String apiKey,       // API key for authentication
            @Query("query") String query,          // Search keyword
            @Query("language") String language,    // Language preference, e.g., "ko-KR"
            @Query("region") String region         // Region, e.g., "KR"
    );
}
