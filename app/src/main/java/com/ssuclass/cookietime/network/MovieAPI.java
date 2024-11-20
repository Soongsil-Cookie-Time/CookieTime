package com.ssuclass.cookietime.network;
import com.ssuclass.cookietime.network.response.KOBISBoxOfficeResponse;
import com.ssuclass.cookietime.network.response.KOBISSearchResponse;
import com.ssuclass.cookietime.network.service.KOBISBoxOfficeService;
import com.ssuclass.cookietime.network.service.KOBISSearchService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class MovieAPI {
    // Base URLs for the APIs
    private static final String KMDb_URL = "http://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/";
    private static final String KOBIS_BOXOFFICE_URL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/";
    private static final String KOBIS_SEARCH_URL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/";

    // Retrofit instances
    private static Retrofit kmdbRetrofit;
    private static Retrofit kobisBoxOfficeRetrofit;
    private static Retrofit kobisSearchRetrofit;

    /**
     * Returns Retrofit instance for KMDb API
     */
    public static Retrofit getKMDbInstance() {
        if (kmdbRetrofit == null) {
            kmdbRetrofit = new Retrofit.Builder()
                    .baseUrl(KMDb_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return kmdbRetrofit;
    }

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
     * Returns Retrofit instance for KOBIS Movie Search API
     */
    public static Retrofit getKOBISSearchInstance() {
        if (kobisSearchRetrofit == null) {
            kobisSearchRetrofit = new Retrofit.Builder()
                    .baseUrl(KOBIS_SEARCH_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return kobisSearchRetrofit;
    }

    public static void fetchBoxOfficeData(String apiKey, Callback<KOBISBoxOfficeResponse> callback) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        String formattedDate = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());

        KOBISBoxOfficeService service = MovieAPI.getKOBISBoxOfficeInstance().create(KOBISBoxOfficeService.class);
        Call<KOBISBoxOfficeResponse> call = service.getBoxOffice(apiKey, formattedDate);
        call.enqueue(callback);
    }

    public static void fetchMovieData(String apiKey, String movieName, Callback<KOBISSearchResponse> callback) {
        KOBISSearchService service = MovieAPI.getKOBISSearchInstance().create(KOBISSearchService.class);
        Call<KOBISSearchResponse> call = service.getMovies(apiKey, movieName);
        call.enqueue(callback);
    }

    

}

