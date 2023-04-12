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
    private int index = 0;
    private Class<?extends  Fragment>[] fragments;
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
        fragments = new Class[]{
                SignUpWelcomeFragment.class,
                SignUpNameFragment.class,
                SignUpBirthdayFragment.class,
                SignupGenderFragment.class
        };
        //set first fragment as active
        if (savedInstanceState == null) {
           setActiveFragment(fragments[index]);
        }
        // listener : when next button in the fragment is clicked
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


    }

    private void OnClickBtnNext(){
        if(index < fragments.length){
            setActiveFragment(fragments[++index]);
        }else{
            finishSignUp();
        }
    }

    private void finishSignUp(){

    }

    private void setActiveFragment(Class<? extends Fragment> fragmentClass){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.slide_out
                )
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, fragmentClass, null)
                .addToBackStack("signup_fragment")
                .commit();
    }
    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        if(fragment.getClass() == SignUpWelcomeFragment.class){
            finish();
        }else{
            index--;
            super.onBackPressed();
        }
    }
}