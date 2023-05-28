package com.example.konekq.BackendAPI.Comments;

import com.example.konekq.BackendAPI.APIResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CommentsAPIService {
    @FormUrlEncoded
    @POST("/comments/all") // needs token
    Call<CommentsAPIResponse> getComments(@Field("post_id") int postId);

    @FormUrlEncoded
    @POST("/comments/add") // needs token
    Call<AddCommentResponse> addComment(
            @Field("post_id") int postId,
            @Field("content") String content
    );

    @FormUrlEncoded
    @POST("/comments/delete") // needs token
    Call<APIResponse> deleteComment(
            @Field("comment_id") int commentId
    );
}
