package com.example.konekq.BackendAPI.Posts;

import com.example.konekq.BackendAPI.APIResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PostsAPIService {

    @Multipart
    @POST("posts/add")
    Call<APIResponse> AddPostWithPhoto(
        @Part MultipartBody.Part photo,
        @Part("content") String content
    );

    @GET("posts/all")
    Call<PostsAPIResponse> getAll();
}
