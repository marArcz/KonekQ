package com.example.konekq;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.konekq.BackendAPI.APIResponse;
import com.example.konekq.BackendAPI.RetrofitClient;
import com.example.konekq.BackendAPI.Users.UserAPIResponse;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;

import java.io.File;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeProfilePicActivity extends AppCompatActivity {
    Button btnBack, btnChange,btnUpdate;
    ImageView imageView;
    Uri selectedUri;

    ActivityResultLauncher<Intent> launcher=
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                if(result.getResultCode()==RESULT_OK){
                    Uri uri=result.getData().getData();
                    if(uri != null){
                        selectedUri = uri;
                        imageView.setImageURI(uri);
                    }
                }else if(result.getResultCode()== ImagePicker.RESULT_ERROR){

                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profie_pic);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            selectedUri = getIntent().getParcelableExtra("photo",Uri.class);
        }else{
            selectedUri = getIntent().getParcelableExtra("photo");
        }

        imageView = findViewById(R.id.imageView);
        btnBack = findViewById(R.id.btn_back);
        btnChange = findViewById(R.id.btnChange);
        btnUpdate = findViewById(R.id.btnUpdate);

        imageView.setImageURI(selectedUri);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(ChangeProfilePicActivity.this)
                        .crop()
                        .cropOval()
                        .maxResultSize(512,512,true)
                        .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                        .createIntentFromDialog(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                launcher.launch(intent);
                                return null;
                            }
                        });
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = AppManager.getToken(getApplicationContext());
                String errorCode = ErrorCodes.CHANGING_PROFILE_PICTURE;
                File photo = FileUtils.getFile(getApplicationContext(),selectedUri);
                RequestBody requestPhoto =
                        RequestBody.create(
                                MediaType.parse("image/jpg"),
                                photo
                        );
                MultipartBody.Part photoBody =
                        MultipartBody.Part.createFormData("photo", photo.getName(), requestPhoto);
                Call<UserAPIResponse> changeProfile = RetrofitClient.getUserService(token).changeProfilePicture(photoBody);
                changeProfile.enqueue(new Callback<UserAPIResponse>() {
                    @Override
                    public void onResponse(Call<UserAPIResponse> call, Response<UserAPIResponse> response) {
                        if(response.body() == null){
                            Toast.makeText(ChangeProfilePicActivity.this, "Server response is empty!", Toast.LENGTH_SHORT).show();
                        }else {
                            if(!response.body().isSuccess()){
                                new CustomAlertDialog(ChangeProfilePicActivity.this)
                                        .setMessage(response.body().getMessage())
                                        .showError();
                            }else{
                                Toast.makeText(ChangeProfilePicActivity.this, "Successfully changed", Toast.LENGTH_SHORT).show();
                                AppManager.saveUser(response.body().getUser(),getApplicationContext());
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserAPIResponse> call, Throwable t) {
                        new CustomAlertDialog(ChangeProfilePicActivity.this)
                                .showError(errorCode);
                        Log.d(errorCode,t.getMessage());
                    }
                });
            }
        });
    }
}