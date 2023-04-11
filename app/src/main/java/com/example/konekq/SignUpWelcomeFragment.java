package com.example.konekq;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SignUpWelcomeFragment extends Fragment {
    public SignUpWelcomeFragment() {
        super(R.layout.signup_welcome_fragment);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        View root = view;

        Button btnNext = root.findViewById(R.id.btn_next);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putString("fragment","welcome_fragment");

                getParentFragmentManager().setFragmentResult("signup_fragment",result);
            }
        });

        super.onViewCreated(root, savedInstanceState);
    }
}
