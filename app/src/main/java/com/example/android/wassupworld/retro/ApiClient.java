package com.example.android.wassupworld.retro;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    private static final String BASE_URL = "https://newsapi.org/v1/";
    private static Retrofit retrofit = null;


    public static <S> S getClient(Class<S> serviceClass) {
        if (retrofit==null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL).client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
    }
        return retrofit.create(serviceClass);
    }}

