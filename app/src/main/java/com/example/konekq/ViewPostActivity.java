package com.example.konekq;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.konekq.BackendAPI.APIResponse;
import com.example.konekq.BackendAPI.Comments.AddCommentResponse;
import com.example.konekq.BackendAPI.Comments.CommentsAPIResponse;
import com.example.konekq.BackendAPI.Posts.PostDetailsAPIResponse;
import com.example.konekq.BackendAPI.Posts.PostsAPIResponse;
import com.example.konekq.BackendAPI.RetrofitClient;
import com.example.konekq.Models.Comment;
import com.example.konekq.Models.Posts;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPostActivity extends AppCompatActivity {
    Button btnBack,btnAddComment;
    int postId;
    EditText editTextComment;
    ImageView imageViewPostPhoto,imageViewProfile;
    TextView textViewName,textViewCaption,textViewNameSecondary;
    ArrayList<Comment> comments;
    RecyclerView recyclerViewComments;
    CommentsRecyclerAdapter commentsRecyclerAdapter;
    String token;
    Posts post;
    CustomProgressDialog progressDialog;
    TextView textViewNoComments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        token = AppManager.getToken(this);
        postId = getIntent().getIntExtra("post_id",0);

        comments = new ArrayList<>();
        recyclerViewComments = findViewById(R.id.recycle_view_comments);
        commentsRecyclerAdapter = new CommentsRecyclerAdapter(this,comments);
        recyclerViewComments.setAdapter(commentsRecyclerAdapter);
        textViewName = findViewById(R.id.textView_name);
        textViewNameSecondary = findViewById(R.id.textView_name_secondary);
        textViewCaption = findViewById(R.id.textView_caption);
        imageViewPostPhoto = findViewById(R.id.imageView_post_photo);
        progressDialog = new CustomProgressDialog(this);
        btnBack = findViewById(R.id.btn_back);
        btnAddComment = findViewById(R.id.btn_add_comment);
        textViewNoComments = findViewById(R.id.textView_no_comments);
        imageViewProfile = findViewById(R.id.imageView_profile);
        editTextComment = findViewById(R.id.editText_comment);
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment(new RequestCompleteListener() {
                    @Override
                    public void onComplete() {
                        if(comments.size() > 0) textViewNoComments.setVisibility(View.GONE);
                        editTextComment.setText("");
                        editTextComment.clearFocus();
                    }
                });
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //load post details
        loadPost(new RequestCompleteListener() {
            @Override
            public void onComplete() {
                //load comments
                loadComments(new RequestCompleteListener() {
                    @Override
                    public void onComplete() {
                        if(comments.size() > 0) textViewNoComments.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void addComment(RequestCompleteListener requestCompleteListener){
        String comment = editTextComment.getText().toString();
        Call<AddCommentResponse> addCommentCall = RetrofitClient.getCommentService(token).addComment(postId,comment);
        String errorCode = ErrorCodes.ADDING_COMMENT;
        addCommentCall.enqueue(new Callback<AddCommentResponse>() {
            @Override
            public void onResponse(Call<AddCommentResponse> call, Response<AddCommentResponse> response) {
                if(response.body() == null){
                    Toast.makeText(ViewPostActivity.this, "Server response is empty!", Toast.LENGTH_SHORT).show();
                }else{
                    if(!response.body().isSuccess()){
                        new CustomAlertDialog(ViewPostActivity.this)
                                .setMessage(response.body().getMessage())
                                .showError();
                    }else{
                        comments.add(response.body().getComment());
                        commentsRecyclerAdapter.notifyItemInserted(comments.size() - 1);
                    }
                }

                if(requestCompleteListener != null) requestCompleteListener.onComplete();
            }

            @Override
            public void onFailure(Call<AddCommentResponse> call, Throwable t) {
                new CustomAlertDialog(ViewPostActivity.this)
                        .showError(errorCode);
                Log.d(errorCode,t.getMessage());
                if(requestCompleteListener != null) requestCompleteListener.onComplete();
            }
        });
    }

    private void loadPost(RequestCompleteListener requestCompleteListener){
        String errorCode = ErrorCodes.GETTING_ONE_POST;
        Call<PostDetailsAPIResponse> getPost = RetrofitClient.getPostService(token).get(postId);

        getPost.enqueue(new Callback<PostDetailsAPIResponse>() {
            @Override
            public void onResponse(Call<PostDetailsAPIResponse> call, Response<PostDetailsAPIResponse> response) {
                if(response.body() == null){
                    Toast.makeText(ViewPostActivity.this, "Server response is empty!", Toast.LENGTH_SHORT).show();
                }else{
                    if(!response.body().isSuccess()){
                        new CustomAlertDialog(ViewPostActivity.this)
                                .setMessage(response.body().getMessage())
                                .showError();
                    }else{
                        post = response.body().getPost();
                        String name = String.format("%s %s", post.getUser().getFirstname(),post.getUser().getLastname());
                        textViewName.setText(name);
                        textViewNameSecondary.setText(name);
                        textViewCaption.setText(post.getContent());
                        Glide.with(getApplicationContext())
                                .load(post.getUser().getProfile_photo())
                                .into(imageViewProfile);
                        if(post.getType() == Posts.TYPE_CAPTION_WITH_PHOTOS){
                            Glide.with(ViewPostActivity.this).load(post.getPhoto()).into(imageViewPostPhoto);
                        }else{
                            imageViewPostPhoto.setVisibility(View.GONE);
                            textViewCaption.setGravity(Gravity.CENTER);
                            textViewCaption.setMinHeight(350);
                            textViewCaption.setTextSize(20f);

                            if(post.getBackground() != null){
                                Glide.with(getApplicationContext())
                                        .load(post.getBackground().getSrc())
                                        .into(new CustomTarget<Drawable>() {
                                            @Override
                                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                textViewCaption.setBackground(resource);
                                            }
                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });
                            }
                        }

                    }
                }

                if(requestCompleteListener != null) requestCompleteListener.onComplete();
            }

            @Override
            public void onFailure(Call<PostDetailsAPIResponse> call, Throwable t) {
                new CustomAlertDialog(ViewPostActivity.this)
                        .showError(errorCode);
                Log.d(errorCode,t.getMessage());
                if(requestCompleteListener != null) requestCompleteListener.onComplete();
            }
        });
    }

    private void loadComments(RequestCompleteListener requestCompleteListener){

        Call<CommentsAPIResponse> getComments = RetrofitClient.getCommentService(token).getComments(postId);
        String errorCode = ErrorCodes.GETTING_COMMENTS;

        getComments.enqueue(new Callback<CommentsAPIResponse>() {
            @Override
            public void onResponse(Call<CommentsAPIResponse> call, Response<CommentsAPIResponse> response) {
                if(response.body() == null){
                    Toast.makeText(ViewPostActivity.this, "Server response is empty!", Toast.LENGTH_SHORT).show();
                }else{
                    if(!response.body().isSuccess()){
                        new CustomAlertDialog(ViewPostActivity.this)
                                .setMessage(response.body().getMessage())
                                .showError();
                    }else{
                        comments = response.body().getComments();
                        commentsRecyclerAdapter = new CommentsRecyclerAdapter(ViewPostActivity.this,comments);
                        recyclerViewComments.setAdapter(commentsRecyclerAdapter);
                        Toast.makeText(ViewPostActivity.this, "Successfully loaded comments!", Toast.LENGTH_SHORT).show();
                    }
                }
                if(requestCompleteListener != null) requestCompleteListener.onComplete();
            }

            @Override
            public void onFailure(Call<CommentsAPIResponse> call, Throwable t) {
                new CustomAlertDialog(ViewPostActivity.this)
                        .showError(errorCode);
                Log.d(errorCode,t.getMessage());
                if(requestCompleteListener != null) requestCompleteListener.onComplete();
            }
        });
    }
}