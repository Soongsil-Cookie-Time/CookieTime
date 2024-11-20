package com.ssuclass.cookietime.network.service;

import com.ssuclass.cookietime.network.response.KMDbResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface KMDbService {
    @GET("search_json.jsp?collection=kmdb_new2")
    Call<KMDbResponse> getMovies(
            @Query("ServiceKey") String serviceKey,
            @Query("releaseDts") String releaseDts,
            @Query("movieNm") String movieNm
    );
}
