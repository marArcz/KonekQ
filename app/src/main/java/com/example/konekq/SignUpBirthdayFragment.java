package com.example.konekq;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.util.Calendar;

public class SignUpBirthdayFragment extends Fragment {
    private String birthday;
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

        Button btnNext = root.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putString("fragment","birthday_fragment");
                result.putString("birthday",birthday);
                getParentFragmentManager().setFragmentResult("signup_fragment",result);
            }
        });
        super.onViewCreated(root, savedInstanceState);
    }
}
