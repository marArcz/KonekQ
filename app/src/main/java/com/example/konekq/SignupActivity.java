package com.example.konekq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SignupActivity extends AppCompatActivity {
    private int id = 0;
    private Fragment[] fragments;
    private Button btnNext;
    ImageButton btnBack;
    String firstname, lastname,birthday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                    )
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView, SignUpWelcomeFragment.class, null)
                    .addToBackStack("welcome_fragment")
                    .commit();
        }
        
        
        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickBtnNext();
            }
        });

        btnBack = findViewById(R.id.toolbar_back_btn);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // We set the listener on the child fragmentManager
        getSupportFragmentManager()
                .setFragmentResultListener("signup_fragment", this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        String result = bundle.getString("bundleKey");
                        // Do something with the result
                        if(bundle.getString("fragment") == "welcome_fragment"){
                        }

                        OnClickBtnNext();
                    }
                });
    }

    private void OnClickBtnNext(){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        if(fragment.getClass() == SignUpWelcomeFragment.class){
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                    )
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView, SignUpNameFragment.class, null)
                    .addToBackStack("signup_fragment")
                    .commit();
        }else if(fragment.getClass() == SignUpNameFragment.class){
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                    )
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView, SignUpBirthdayFragment.class, null)
                    .addToBackStack("signup_fragment")
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        if(fragment.getClass() == SignUpWelcomeFragment.class){
            finish();
        }else{
            super.onBackPressed();
        }
    }
}