package com.example.konekq;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ViewPhotoActivity extends AppCompatActivity {
    Button btnBack;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        String imgUrl = getIntent().getStringExtra("image");

        btnBack = findViewById(R.id.btn_back);
        imageView = findViewById(R.id.imageView);

        Glide.with(this).load(imgUrl).into(imageView);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}