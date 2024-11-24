package com.ssuclass.cookietime.network;

import android.annotation.SuppressLint;

import com.ssuclass.cookietime.network.response.KOBISBoxOfficeResponse;
import com.ssuclass.cookietime.network.response.TMDBMovieDetailResponse;
import com.ssuclass.cookietime.network.response.TMDBMovieSearchResponse;
import com.ssuclass.cookietime.network.service.KOBISBoxOfficeService;
import com.ssuclass.cookietime.network.service.TMDBMovieDetailService;
import com.ssuclass.cookietime.network.service.TMDBMovieSearchService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;

public class MovieAPI {
    // Base URLs for the APIs
    private static final String KOBIS_BOXOFFICE_URL = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/";
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";

    // Retrofit instances
    private static Retrofit kobisBoxOfficeRetrofit;
    private static Retrofit tmdbRetrofit;

    /**
     * Returns Retrofit instance for KOBIS Box Office API
     */
    public static Retrofit getKOBISBoxOfficeInstance() {
        if (kobisBoxOfficeRetrofit == null) {
            kobisBoxOfficeRetrofit = new Retrofit.Builder()
                    .baseUrl(KOBIS_BOXOFFICE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return kobisBoxOfficeRetrofit;
    }

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
     * Fetch daily box office data from KOBIS
     */
    public static void fetchBoxOfficeData(String apiKey, Callback<KOBISBoxOfficeResponse> callback) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        @SuppressLint("SimpleDateFormat") String formattedDate = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());

        KOBISBoxOfficeService service = MovieAPI.getKOBISBoxOfficeInstance().create(KOBISBoxOfficeService.class);
        Call<KOBISBoxOfficeResponse> call = service.getBoxOffice(apiKey, formattedDate);
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
