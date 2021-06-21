package com.example.coronaupdate1.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance = null;
    private CovidApiInterface covidApiInterface;

    private RetrofitClient(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(covidApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        covidApiInterface = retrofit.create(CovidApiInterface.class);
    }

    public static synchronized RetrofitClient getInstance(){
        if(instance == null){
            instance = new RetrofitClient();
        }
        return instance;
    }

    public CovidApiInterface getMyApi(){
        return covidApiInterface;
    }
}
