package com.example.android.wassupworld.retro;

import com.example.android.wassupworld.model.ResultNews;
import com.example.android.wassupworld.model.ResultSources;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiInterface {

    @GET("sources")
    Call<ResultSources> getSources(@Query("apiKey") String apiKey, @Query("language") String language) ;

    @GET("articles")
    Call<ResultNews> getNews(@Query("apiKey") String apiKey, @Query("source") String source ) ;
}
