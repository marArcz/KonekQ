package com.example.konekq;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.konekq.BackendAPI.APIResponse;
import com.example.konekq.BackendAPI.Posts.PostBackgroundAPIResponse;
import com.example.konekq.BackendAPI.Posts.PostsAPIResponse;
import com.example.konekq.BackendAPI.RetrofitClient;
import com.example.konekq.Models.PostBackground;
import com.example.konekq.Models.User;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPostActivity extends AppCompatActivity {
    Button btnPostTop,btnPost,btnAddPhoto,btnBack;
    ImageButton btnRemovePhoto,btnChangePhoto;
    EditText editTextPost;
    ImageView imageViewProfile,imageViewPostPhoto;
    TextView textViewName;
    Uri selectedImage;
    RecyclerView recyclerViewPostBg;
    ArrayList<PostBackground> postBackgrounds;
    RelativeLayout postPhotoContainer;
    int selectedBgIndex=0;
    boolean shouldShowPermissionRationale = false;

    CustomProgressDialog progressDialog;

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    if(uri != null){
                        selectedImage = uri;
                        imageViewPostPhoto.setImageURI(uri);
                        postPhotoContainer.setVisibility(View.VISIBLE);
                        disablePostBackground();
                        btnAddPhoto.setText("Change Photo");
                    }
                }
            });
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent intent = result.getData();
        }
    });

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    mGetContent.launch("image/*");
                    shouldShowPermissionRationale = false;
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    new CustomAlertDialog(AddPostActivity.this)
                            .setTitle("Permission is needed")
                            .setMessage("This app needs permission to access your storage. Without it creating a post with image will not be possible.")
                            .showSimple();
                    shouldShowPermissionRationale = true;
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        User user = AppManager.getUser(getApplicationContext());
        progressDialog = new CustomProgressDialog(this);
        btnBack = findViewById(R.id.btn_back);
        btnPostTop = findViewById(R.id.btn_post_top);
        btnPost = findViewById(R.id.btn_post);
        btnAddPhoto = findViewById(R.id.btn_add_photo);
        editTextPost = findViewById(R.id.editText_content);
        imageViewProfile = findViewById(R.id.imageView_profile);
        imageViewPostPhoto = findViewById(R.id.imageView_post_photo);
        textViewName = findViewById(R.id.textView_name);
        postBackgrounds = new ArrayList<>();
        recyclerViewPostBg = findViewById(R.id.recycle_view_post_backgrounds);
        postPhotoContainer = findViewById(R.id.post_photo_container);
        btnChangePhoto = findViewById(R.id.btn_change_photo);
        btnRemovePhoto = findViewById(R.id.btn_remove_photo);

        Glide.with(getApplicationContext()).load(user.getProfile_photo()).into(imageViewProfile);
        textViewName.setText(String.format("%s %s", user.getFirstname(),user.getLastname()));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnRemovePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImage = null;
                setPostBackground(selectedBgIndex);
                postPhotoContainer.setVisibility(View.GONE);
                btnAddPhoto.setText("Add a photo");
            }
        });

        btnChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });

        PostBgRecyclerAdapter postBgRecyclerAdapter = new PostBgRecyclerAdapter(this, postBackgrounds, new PostBgRecyclerAdapter.OnItemSelectedListener() {
            @Override
            public void onSelect(PostBackground postBg, int position) {
                Toast.makeText(AddPostActivity.this, "Bg selected!", Toast.LENGTH_SHORT).show();
                setPostBackground(position);
                //reset selected
                for(int x=0;x<postBackgrounds.size();x++){
                    postBackgrounds.get(x).setSelected(false);
                }
                //set selected bg
                postBackgrounds.get(position).setSelected(true);
            }
        });
        recyclerViewPostBg.setAdapter(postBgRecyclerAdapter);

        imageViewPostPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPostActivity.this,ViewPhotoActivity.class);
                intent.putExtra("image",selectedImage.toString());
                startActivity(intent);
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPost();
            }
        });

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    mGetContent.launch("image/*");
                }else if(shouldShowPermissionRationale){
                    new CustomAlertDialog(AddPostActivity.this)
                            .setTitle("Permission is needed")
                            .setMessage("To use this feature, you need to grant the app permission to access your storage.")
                            .setOkayBtnClickListener(new CustomAlertDialog.OkayBtnClickListener() {
                                @Override
                                public void onClick(CustomAlertDialog dialog) {
                                    requestPermissionLauncher.launch(
                                            Manifest.permission.READ_EXTERNAL_STORAGE);
                                }
                            })
                            .showSimple();
                }
                else{
                    requestPermissionLauncher.launch(
                            Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
        });
        postBgRecyclerAdapter.setOnItemSelectedListener(new PostBgRecyclerAdapter.OnItemSelectedListener() {
            @Override
            public void onSelect(PostBackground postBg, int position) {
                Toast.makeText(AddPostActivity.this, "Bg selected!", Toast.LENGTH_SHORT).show();
                setPostBackground(position);
                //reset selected
                for(int x=0;x<postBackgrounds.size();x++){
                    postBackgrounds.get(x).setSelected(false);
                }
                //set selected bg
                postBackgrounds.get(position).setSelected(true);
                postBgRecyclerAdapter.notifyDataSetChanged();
            }
        });

        loadBackgrounds();
    }

    private void AddPost(){
        String token = AppManager.getToken(getApplicationContext());
        String content = editTextPost.getText().toString();
        int backgroundId = postBackgrounds.get(selectedBgIndex).getId();
        progressDialog.show();
        if(selectedImage == null){
            Call<APIResponse> addPost = RetrofitClient.getPostService(token)
                    .AddPost(content,backgroundId);

            addPost.enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    progressDialog.dismiss();
                    if(response.body() == null){
                        Toast.makeText(AddPostActivity.this, "Server response is empty!", Toast.LENGTH_SHORT).show();
                    }else{
                        if(response.body().isSuccess()){
                            Toast.makeText(AddPostActivity.this, "Your post is successfully added", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(AddPostActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    String errorCode = ErrorCodes.ADDING_CAPTION_ONLY_POST;
                    new CustomAlertDialog(AddPostActivity.this)
                            .showError(errorCode);
                    Log.d(errorCode,t.getMessage());
                }
            });
        }else{
            File photo = FileUtils.getFile(this,selectedImage);
            RequestBody requestPhoto =
                    RequestBody.create(
                            MediaType.parse("image/jpg"),
                            photo
                    );
            MultipartBody.Part photoBody =
                    MultipartBody.Part.createFormData("photo", photo.getName(), requestPhoto);

            RequestBody postContent =
                    RequestBody.create(
                            MultipartBody.FORM, content);

            Call<APIResponse> addPost = RetrofitClient.getPostService(token)
                    .AddPostWithPhoto(photoBody,postContent);
            addPost.enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    progressDialog.dismiss();
                    if(response.body() == null){
                        Toast.makeText(AddPostActivity.this, "Server response is empty!", Toast.LENGTH_SHORT).show();
                    }else{
                        if(response.body().isSuccess()){
                            Toast.makeText(AddPostActivity.this, "Your post is successfully added", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(AddPostActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    String errorCode = ErrorCodes.ADDING_CAPTION_WITH_PHOTO;
                    new CustomAlertDialog(AddPostActivity.this)
                            .showError(errorCode);
                    Log.d(errorCode,t.getMessage());
                }
            });
        }

    }

    private void disablePostBackground() {
        recyclerViewPostBg.setVisibility(View.GONE);
        editTextPost.setGravity(Gravity.TOP);
        editTextPost.setBackground(getDrawable(R.drawable.edittext_bg));
        editTextPost.setTextColor(Color.BLACK);
        editTextPost.setHintTextColor(Color.BLACK);

    }

    private void setPostBackground(int position){
        selectedBgIndex = position;
        enablePostBackground();
        PostBackground postBg = postBackgrounds.get(position);

        if(postBg.getSrc() == null || position == 0){
            editTextPost.setBackground(getDrawable(R.drawable.edittext_bg));
            editTextPost.setGravity(Gravity.TOP);
            editTextPost.setTextColor(Color.BLACK);
            editTextPost.setHintTextColor(Color.BLACK);
        }else{
            Glide.with(getApplicationContext()).load(postBg.getSrc())
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            editTextPost.setBackground(resource);
                            editTextPost.setGravity(Gravity.CENTER);
                            editTextPost.setTextColor(postBg.getText_color() == PostBackground.TEXT_BLACK? Color.BLACK:Color.WHITE);
                            editTextPost.setHintTextColor(postBg.getText_color() == PostBackground.TEXT_BLACK? Color.BLACK:Color.WHITE);

                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });

        }
    }

    private void enablePostBackground() {
        recyclerViewPostBg.setVisibility(View.VISIBLE);
        editTextPost.setGravity(Gravity.TOP);
        editTextPost.setBackground(getDrawable(R.drawable.edittext_bg));
    }

    private void loadBackgrounds(){
        Call<PostBackgroundAPIResponse> getBackgrounds = RetrofitClient.getPostService().getBackgrounds();
        getBackgrounds.enqueue(new Callback<PostBackgroundAPIResponse>() {
            @Override
            public void onResponse(Call<PostBackgroundAPIResponse> call, Response<PostBackgroundAPIResponse> response) {
                if(response.body() == null){
                    Toast.makeText(AddPostActivity.this, "Server response is empty!", Toast.LENGTH_SHORT).show();
                }else{
                    postBackgrounds = response.body().getBackgrounds();
                    postBackgrounds.add(0,new PostBackground(0,null,0));
                    PostBgRecyclerAdapter postBgRecyclerAdapter = new PostBgRecyclerAdapter(AddPostActivity.this,postBackgrounds,new PostBgRecyclerAdapter.OnItemSelectedListener() {
                        @Override
                        public void onSelect(PostBackground postBg, int position) {
                            setPostBackground(position);
                            //reset selected
                            for(int x=0;x<postBackgrounds.size();x++){
                                postBackgrounds.get(x).setSelected(false);
                            }
                            //set selected bg
                            postBackgrounds.get(position).setSelected(true);
                        }
                    });
                    recyclerViewPostBg.setAdapter(postBgRecyclerAdapter);
                }
            }

            @Override
            public void onFailure(Call<PostBackgroundAPIResponse> call, Throwable t) {
                String errorCode = ErrorCodes.GETTING_POST_BACKGROUNDS;
                new CustomAlertDialog(AddPostActivity.this)
                        .showError(errorCode);
            }
        });
    }

}