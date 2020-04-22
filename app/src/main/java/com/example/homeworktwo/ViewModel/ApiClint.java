package com.example.homeworktwo.ViewModel;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClint {

    //public static final String BASE_URL = "http://192.168.0.100/BasicDatabase/";
    public static final String BASE_URL = "http://192.168.43.61:8085/";
    public static Retrofit retrofit;

    public static ApiInterface getApiClint(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(ApiInterface.class);
    }
}
