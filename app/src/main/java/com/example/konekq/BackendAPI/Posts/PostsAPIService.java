package com.example.konekq.BackendAPI.Posts;

import com.example.konekq.BackendAPI.APIResponse;
import com.example.konekq.Models.PostBackground;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PostsAPIService {

    @Multipart
    @POST("posts/add")
    Call<APIResponse> AddPostWithPhoto(
        @Part MultipartBody.Part photo,
        @Part("content") RequestBody content
    );

    @FormUrlEncoded
    @POST("posts/add")
    Call<APIResponse> AddPost(
        @Field("content") String content,
        @Field("background_id") int backgroundId
    );

    @GET("posts/all")
    Call<PostsAPIResponse> getAll();

    @GET("posts/backgrounds")
    Call<PostBackgroundAPIResponse> getBackgrounds();

}
