package com.example.konekq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.konekq.BackendAPI.RetrofitClient;
import com.example.konekq.BackendAPI.Users.UserAPIResponse;
import com.example.konekq.Models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private final int id = 0;
    private int index = 0;
    private Class<?extends  Fragment>[] fragments;
    private Button btnNext;
    ImageButton btnBack;

    String firstname, lastname,birthday,gender,password,emailAddress;
    CustomProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressDialog = new CustomProgressDialog(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        fragments = new Class[]{
                SignUpWelcomeFragment.class,
                SignUpNameFragment.class,
                SignUpBirthdayFragment.class,
                SignupGenderFragment.class,
                SignUpEmailFragment.class,
                SignupPasswordFragment.class
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
                        // Check
                        switch (bundle.getString("fragment")){
                            case "name_fragment":
                                firstname = bundle.getString("firstname");
                                lastname = bundle.getString("lastname");
                                break;
                            case "birthday_fragment":
                                birthday = bundle.getString("birthday");
                                break;
                            case "gender_fragment":
                                gender = bundle.getString("gender");
                                break;
                            case "email_fragment":
                                emailAddress = bundle.getString("email");
                            case "password_fragment":
                                password = bundle.getString("password");
                                break;
                        }
                        if(bundle.getString("fragment") == "finishing_up_fragment"){
                            createUserAccount();
                        }else{
                            OnClickBtnNext();
                        }
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

    private void createUserAccount() {
        //show progress dialog
        progressDialog.show();

        User newUser = new User();
        newUser.setFirstname(firstname);
        newUser.setLastname(lastname);
        newUser.setEmail_address(emailAddress);
        newUser.setGender(gender);
        newUser.setBirthday(birthday);
        newUser.setPassword(password);

        Call<UserAPIResponse> signUpCall = RetrofitClient.getUserService().SignUp(newUser);


        signUpCall.enqueue(new Callback<UserAPIResponse>() {
            @Override
            public void onResponse(Call<UserAPIResponse> call, Response<UserAPIResponse> response) {
                if(response.body() == null){
                    new CustomAlertDialog(SignupActivity.this)
                            .setTitle("Failed")
                            .setMessage("Server response is empty!")
                            .showError();
                    Log.d(ErrorCodes.SIGNUP,"Server response is empty!");
                }else{
                    if(response.body().isSuccess()){
                        AppManager.saveUser(response.body().getUser(),getApplicationContext());
                        AppManager.saveToken(response.body().getToken(),getApplicationContext());
                        new CustomAlertDialog(SignupActivity.this)
                                .setTitle("Success")
                                .setMessage("We successfully created your account!")
                                .setOkayBtnClickListener(new CustomAlertDialog.OkayBtnClickListener() {
                                    @Override
                                    public void onClick(CustomAlertDialog dialog) {
                                        dialog.dismiss();
                                        startActivity(new Intent(SignupActivity.this, ActivityVerifyEmail.class));
                                    }
                                })
                                .showSuccess();
                    }else{
                        new CustomAlertDialog(SignupActivity.this)
                                .setTitle("Oh Snap")
                                .setMessage(response.body().getMessage())
                                .showError();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserAPIResponse> call, Throwable t) {
                progressDialog.dismiss();
                new CustomAlertDialog(SignupActivity.this)
                        .setMessage(t.getMessage())
                        .showError();
                System.out.println("Error 1001: " + call);
                Log.d(ErrorCodes.SIGNUP,t.getMessage());
            }
        });

    }

    private void OnClickBtnNext(){
        if(index < fragments.length-1){
            setActiveFragment(fragments[++index]);
        }else{
            finishSignUp();
        }
    }

    private void finishSignUp(){
        setActiveFragment(FinishingUpFragment.class);
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