package com.example.konekq;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SignUpEmailFragment extends Fragment {

    public SignUpEmailFragment() {
        super(R.layout.signup_email_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        View root = view;
        EditText editTextEmail = root.findViewById(R.id.edit_text_email);
        Button btnNext = root.findViewById(R.id.btn_next);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextEmail.getText().toString().isBlank()){
                    Bundle result = new Bundle();
                    result.putString("fragment","email_fragment");
                    result.putString("email",editTextEmail.getText().toString());
                    getParentFragmentManager().setFragmentResult("signup_fragment",result);
                }else{
                    editTextEmail.setError("You need to enter your email address!");
                }
            }
        });

        super.onViewCreated(root, savedInstanceState);

    }



}
