package com.example.konekq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.konekq.BackendAPI.RetrofitClient;
import com.example.konekq.BackendAPI.Users.UserAPIResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Intent intent;
        String token = AppManager.getToken(getApplicationContext());
        if(token != null){
            //if user is logged in redirect to homepage
            intent = new Intent(ActivitySplashScreen.this, HomeActivity.class);
            Call<UserAPIResponse> getAccount = RetrofitClient.getUserService(token).getAccount();
            getAccount.enqueue(new Callback<UserAPIResponse>() {
                @Override
                public void onResponse(Call<UserAPIResponse> call, Response<UserAPIResponse> response) {
                    if(response.body() != null){
                        AppManager.saveUser(response.body().getUser(), getApplicationContext());
                        proceed(intent,1000);
                    }else{
                        Toast.makeText(ActivitySplashScreen.this, "Server response is empty!", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<UserAPIResponse> call, Throwable t) {
                    Toast.makeText(ActivitySplashScreen.this, "Something went wrong pelase try again later!", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            //redirect to login page
            intent = new Intent(this, LoginActivity.class);
            proceed(intent,1500);
        }

    }

    private void proceed(Intent intent, int s){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        },s);
    }

}