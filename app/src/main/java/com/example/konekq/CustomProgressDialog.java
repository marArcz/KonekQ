package com.example.konekq;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public class CustomProgressDialog extends Dialog {

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
}
