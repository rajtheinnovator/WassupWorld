package com.example.android.wassupworld.retro;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dell on 7/2/2017.
 */

public class ApiClient {

    public static final String BASE_URL = "https://newsapi.org/v1/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
    int x=0;
        x++;}
        return retrofit;
    }}

