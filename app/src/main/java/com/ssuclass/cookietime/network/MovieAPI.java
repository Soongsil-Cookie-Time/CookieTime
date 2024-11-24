package com.ssuclass.cookietime.network;

import com.ssuclass.cookietime.network.response.TMDBMovieDetailResponse;
import com.ssuclass.cookietime.network.response.TMDBMovieSearchResponse;
import com.ssuclass.cookietime.network.response.TMDBNowPlayingResponse;
import com.ssuclass.cookietime.network.service.TMDBMovieDetailService;
import com.ssuclass.cookietime.network.service.TMDBNowPlayingService;
import com.ssuclass.cookietime.network.service.TMDBMovieSearchService;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;

public class MovieAPI {
    // Base URLs for the APIs
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";

    // Retrofit instances
    private static Retrofit tmdbRetrofit;

    /**
     * Returns Retrofit instance for TMDB API
     */
    public static Retrofit getTMDBInstance() {
        if (tmdbRetrofit == null) {
            tmdbRetrofit = new Retrofit.Builder()
                    .baseUrl(TMDB_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return tmdbRetrofit;
    }

    /**
     * Fetch now playing movies from TMDB
     */
    public static void fetchNowPlayingMovies(String apiKey, String language, String region, int page, Callback<TMDBNowPlayingResponse> callback) {
        TMDBNowPlayingService service = MovieAPI.getTMDBInstance().create(TMDBNowPlayingService.class);
        Call<TMDBNowPlayingResponse> call = service.getNowPlayingMovies(apiKey, language, region, page);
        call.enqueue(callback);
    }

    /**
     * Fetch movie detail data from TMDB
     */
    public static void fetchMovieDetail(int movieId, String apiKey, String language, Callback<TMDBMovieDetailResponse> callback) {
        TMDBMovieDetailService service = MovieAPI.getTMDBInstance().create(TMDBMovieDetailService.class);
        Call<TMDBMovieDetailResponse> call = service.getMovieDetail(movieId, apiKey, language);
        call.enqueue(callback);
    }

    /**
     * Search movies using TMDB
     */
    public static void searchMovies(String query, String apiKey, String language, String region, Callback<TMDBMovieSearchResponse> callback) {
        TMDBMovieSearchService service = MovieAPI.getTMDBInstance().create(TMDBMovieSearchService.class);
        Call<TMDBMovieSearchResponse> call = service.searchMovies(apiKey, query, language, region);
        call.enqueue(callback);
    }
}
