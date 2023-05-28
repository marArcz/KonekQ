package com.example.konekq.BackendAPI.Users;

import com.example.konekq.BackendAPI.APIResponse;
import com.example.konekq.Models.User;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserAPIService {

    @POST("users/account")
    Call<UserAPIResponse> getAccount();

    @POST("users/signup")
    Call<UserAPIResponse> SignUp(@Body User user);

    @FormUrlEncoded
    @POST("users/login")
    Call<UserAPIResponse> Login(
            @Field("email_address") String emailAddress,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("users/check-email")
    Call<APIResponse> checkEmailAddress(@Field("email_address") String emailAddress);

    //needs user bearer token
    @POST("users/verification/send")
    Call<APIResponse> sendVerificationCode();

    //needs user bearer token
    @FormUrlEncoded
    @POST("users/verification/verify")
    Call<APIResponse> verifyEmail(@Field("verification_code") String verificationCode);

    //needs user bearer token
    @Multipart
    @POST("users/change-profile")
    Call<UserAPIResponse> changeProfilePicture(@Part MultipartBody.Part photo);

}
