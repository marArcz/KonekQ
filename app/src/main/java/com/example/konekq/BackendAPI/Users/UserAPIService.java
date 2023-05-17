package com.example.konekq.BackendAPI.Users;

import com.example.konekq.Models.User;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserAPIService {

    @POST("users/signup")
    Call<UserAPIResponse> SignUp(@Body User user);

    @FormUrlEncoded
    @POST("users/login")
    Call<UserAPIResponse> Login(
            @Field("email_address") String emailAddress,
            @Field("password") String password
    );

}
