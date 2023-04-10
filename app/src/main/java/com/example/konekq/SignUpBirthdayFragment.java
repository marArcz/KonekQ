package com.example.konekq;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.type.DateTime;

import java.time.LocalDate;
import java.util.Calendar;

public class SignUpBirthdayFragment extends Fragment {
    public SignUpBirthdayFragment() {
        super(R.layout.signup_birthday_fragment);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        View root = view;

        DatePicker datePicker = root.findViewById(R.id.datePicker);
        TextView textViewAge = root.findViewById(R.id.text_view_age);
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Getting year from date
                int currentYr = Calendar.getInstance().get(Calendar.YEAR);
                textViewAge.setText((currentYr - year) + " years old");
            }
        });
        super.onViewCreated(root, savedInstanceState);
    }
}
