package com.ssuclass.cookietime.network;
import android.annotation.SuppressLint;

import com.ssuclass.cookietime.network.response.KOBISBoxOfficeResponse;
import com.ssuclass.cookietime.network.service.KOBISBoxOfficeService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.Call;

public class MovieAPI {
    // Base URLs for the APIs
    private static final String KOBIS_BOXOFFICE_URL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/";

    // Retrofit instances
    private static Retrofit kobisBoxOfficeRetrofit;

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

    public static void fetchBoxOfficeData(String apiKey, Callback<KOBISBoxOfficeResponse> callback) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        @SuppressLint("SimpleDateFormat") String formattedDate = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());

        KOBISBoxOfficeService service = MovieAPI.getKOBISBoxOfficeInstance().create(KOBISBoxOfficeService.class);
        Call<KOBISBoxOfficeResponse> call = service.getBoxOffice(apiKey, formattedDate);
        call.enqueue(callback);
    }
}

