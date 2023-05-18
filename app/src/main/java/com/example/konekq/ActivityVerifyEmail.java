package com.example.konekq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.konekq.BackendAPI.APIResponse;
import com.example.konekq.BackendAPI.RetrofitClient;
import com.example.konekq.Models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityVerifyEmail extends AppCompatActivity {
    TextView textViewMessage;
    Button btnVerify, btnSend;
    EditText editTextVerifyEmail;
    User user;
    String token;
    CustomProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        progressDialog = new CustomProgressDialog(this);
        token = AppManager.getToken(getApplicationContext());

        textViewMessage = findViewById(R.id.textViewMessage);
        btnVerify = findViewById(R.id.btn_verify);
        btnSend = findViewById(R.id.btn_send_again);
        editTextVerifyEmail = findViewById(R.id.editText_verify_email);

        user = AppManager.getUser(getApplicationContext());

        if(user.getIs_verified() == User.VERIFIED_TRUE){
            startActivity(new Intent(ActivityVerifyEmail.this,HomeActivity.class));
        }else{
            Toast.makeText(this, "Sending", Toast.LENGTH_SHORT).show();
            sendVerificationCode();
        }

        textViewMessage.setText(String.format(textViewMessage.getText().toString(), user.getEmail_address()));

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verificationCode = editTextVerifyEmail.getText().toString();
                if(verificationCode.isBlank()){
                    editTextVerifyEmail.setError("You need to enter your verification code");
                }else{
                    Call<APIResponse> verifyEmail = RetrofitClient.getUserService(token).verifyEmail(verificationCode);
                    verifyEmail.enqueue(new Callback<APIResponse>() {
                        @Override
                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                            if(response.body() == null){
                                new CustomAlertDialog(ActivityVerifyEmail.this)
                                        .showError(ErrorCodes.VERIFYING_EMAIL_ADDRESS);
                            }else{
                                if(response.body().isSuccess()){
                                    new CustomAlertDialog(ActivityVerifyEmail.this)
                                            .setMessage("Successfully verified")
                                            .setOkayBtnClickListener(new CustomAlertDialog.OkayBtnClickListener() {
                                                @Override
                                                public void onClick(CustomAlertDialog dialog) {
                                                    startActivity(new Intent(ActivityVerifyEmail.this,HomeActivity.class));
                                                }
                                            })
                                            .showSuccess();
                                }else{
                                    new CustomAlertDialog(ActivityVerifyEmail.this)
                                            .setMessage(response.body().getMessage())
                                            .showError();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<APIResponse> call, Throwable t) {
                            String errorCode = ErrorCodes.VERIFYING_EMAIL_ADDRESS;
                            new CustomAlertDialog(ActivityVerifyEmail.this)
                                    .showError(errorCode);

                            Log.d(errorCode,t.getMessage());
                        }
                    });
                }
            }
        });


    }

    public void sendVerificationCode(){
        progressDialog.show();
        //send verification code
        Call<APIResponse> sendVerificationCode = RetrofitClient.getUserService(token).sendVerificationCode();

        sendVerificationCode.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                progressDialog.dismiss();
              if(response.body() != null){
                  if(!response.body().isSuccess()){
                      String errorCode = ErrorCodes.SENDING_VERIFICATION_CODE;
                      new CustomAlertDialog(ActivityVerifyEmail.this)
                              .showError(response.body().getMessage());
                      Log.d(errorCode,response.body().getMessage());
                  }else{
                      new CustomAlertDialog(ActivityVerifyEmail.this)
                              .setTitle("Successfully sent!")
                              .setMessage("Please check your inbox for your verification code.")
                              .setDrawable(R.drawable.undraw_high_five_re_jy71)
                              .show();
                  }
              }else{
                  new CustomAlertDialog(ActivityVerifyEmail.this)
                          .setMessage(response.message())
                          .showError();
              }
            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                progressDialog.dismiss();
                String errorCode = ErrorCodes.SENDING_VERIFICATION_CODE;
                new CustomAlertDialog(ActivityVerifyEmail.this)
                        .showError(errorCode);
                Log.d(errorCode,t.getMessage());
            }
        });
    }
}