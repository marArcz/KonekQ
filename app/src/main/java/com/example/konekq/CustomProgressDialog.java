package com.example.konekq;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.progressindicator.LinearProgressIndicator;

public class CustomProgressDialog extends Dialog {
    LinearProgressIndicator linearProgressIndicator;

    public CustomProgressDialog(@NonNull Context context) {
        super(context,R.style.custom_dialog_style);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.horizontalMargin = 0;
        params.gravity = Gravity.TOP;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);

        View view = LayoutInflater.from(context).inflate(R.layout.progress_dialog_layout,null,false);
        setContentView(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        linearProgressIndicator = findViewById(R.id.progress_indicator);
    }

    @Override
    public void show() {
        super.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    linearProgressIndicator.setProgress(80,true);
                }else {
                    linearProgressIndicator.setProgress(80);
                }
            }
        },300);
    }

    @Override
    public void dismiss() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            linearProgressIndicator.setProgress(100,true);
        }else {
            linearProgressIndicator.setProgress(100);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                linearProgressIndicator.setProgress(10);
                CustomProgressDialog.super.dismiss();
            }
        },500);
    }
}
