package com.example.konekq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.konekq.BackendAPI.RetrofitClient;
import com.example.konekq.BackendAPI.Users.UserAPIResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button btnSignUp, btnLogin;
    EditText editTextEmail, editTextPassword;
    CustomProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new CustomProgressDialog(this);
        editTextEmail = findViewById(R.id.edit_textEmail);
        editTextPassword = findViewById(R.id.edit_textPassword);

        btnSignUp = findViewById(R.id.btn_signup);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });

        btnLogin = findViewById(R.id.btn_verify);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                if(email.isBlank()){
                    editTextEmail.setError("You need to enter your email address");
                }else if(password.isBlank()){
                    editTextPassword.setError("You need to enter your password");
                }else{
                    progressDialog.show();
                    Call<UserAPIResponse> login = RetrofitClient.getUserService().Login(email,password);

                    login.enqueue(new Callback<UserAPIResponse>() {
                        @Override
                        public void onResponse(Call<UserAPIResponse> call, Response<UserAPIResponse> response) {
                            progressDialog.dismiss();
                            if(response.body() == null){
                                String error_code = ErrorCodes.LOGIN;
                                new CustomAlertDialog(LoginActivity.this)
                                        .showError(error_code);
                            }else{
                                if(response.body().isSuccess()){
                                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                }else{
                                    new CustomAlertDialog(LoginActivity.this)
                                            .setMessage(response.body().getMessage())
                                            .showError();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserAPIResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            String error_code = ErrorCodes.LOGIN;
                            new CustomAlertDialog(LoginActivity.this)
                                    .showError(error_code);
                            Log.d(error_code,t.getMessage());
                        }
                    });
                }
            }
        });

    }
}