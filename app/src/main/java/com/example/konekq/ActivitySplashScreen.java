package com.example.konekq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class ActivitySplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent intent;

        if(AppManager.getUser(this) != null){
            //if user is logged in redirect to homepage
            intent = new Intent(ActivitySplashScreen.this, HomeActivity.class);
        }else{
            //redirect to login page
            intent = new Intent(this, LoginActivity.class);
        }

        //redirect user after a 0.6 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        },600);
    }
}