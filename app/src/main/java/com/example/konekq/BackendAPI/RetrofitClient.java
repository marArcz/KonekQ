package com.example.konekq.BackendAPI;

import android.util.Log;

import com.example.konekq.BackendAPI.Users.UserAPIService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static String baseUrl = "https://unchartered-threes.000webhostapp.com/";
//    static String baseUrl = "http://192.168.254.193/konekq_backend/";
    public static Retrofit getRetrofit(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    public static Retrofit getRetrofit(String token){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest=chain.request().newBuilder()
                                .header("Authorization","Bearer "+ token)
                                .build();
                        return chain.proceed(newRequest);
                    }
                });
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    public static UserAPIService getUserService(){
        return getRetrofit().create(UserAPIService.class);
    }
    public static UserAPIService getUserService(String token){
        return getRetrofit(token).create(UserAPIService.class);
    }
}
