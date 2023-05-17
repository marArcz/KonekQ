package com.example.konekq;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

public class SignUpNameFragment extends Fragment {
    public SignUpNameFragment() {
        super(R.layout.signup_name_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        View root = view;

        Button btnNext = root.findViewById(R.id.btn_next);
        EditText firstnameBox = root.findViewById(R.id.edit_text_firstname);
        EditText lastNameBox = root.findViewById(R.id.edit_text_lastname);
        Snackbar snackbar = Snackbar.make(root,"Please enter your name to continue!",Snackbar.LENGTH_SHORT);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putString("fragment","name_fragment");

                if (firstnameBox.getText().toString().isEmpty() || lastNameBox.getText().toString().isEmpty()){
                    if(firstnameBox.getText().toString().isEmpty() || firstnameBox.getText().toString().length() < 2){
                        firstnameBox.setError("Please enter your firstname!");
                    }else if(lastNameBox.getText().toString().isEmpty() || lastNameBox.getText().toString().length() < 2){
                        lastNameBox.setError("Please enter your lastname!");
                    }
                    snackbar.show();
                }else{
                    result.putString("firstname",firstnameBox.getText().toString());
                    result.putString("lastname",lastNameBox.getText().toString());
                    getParentFragmentManager().setFragmentResult("signup_fragment",result);
                }

            }
        });

        super.onViewCreated(root, savedInstanceState);
    }
}
