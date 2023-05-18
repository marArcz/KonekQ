package com.example.konekq;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.konekq.BackendAPI.APIResponse;
import com.example.konekq.BackendAPI.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpEmailFragment extends Fragment {
    EditText editTextEmail;
    CustomProgressDialog progressDialog;
    public SignUpEmailFragment() {
        super(R.layout.signup_email_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        View root = view;
        editTextEmail = root.findViewById(R.id.edit_text_email);
        Button btnNext = root.findViewById(R.id.btn_next);

        progressDialog = new CustomProgressDialog(getActivity());

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String email = editTextEmail.getText().toString();
                if(!email.isBlank()){
                    Call<APIResponse> checkEmail = RetrofitClient.getUserService().checkEmailAddress(email);
                    checkEmail.enqueue(new Callback<APIResponse>() {
                        @Override
                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                            progressDialog.dismiss();
                            if(response.body().isSuccess()){
                                proceedNext();
                            }else{
                                editTextEmail.setError("Sorry this email address is already taken");
                            }
                        }
                        @Override
                        public void onFailure(Call<APIResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            String errorCode = ErrorCodes.CHECK_EMAIL_ADDRESS;
                            new CustomAlertDialog(getContext())
                                    .showError(errorCode);
                            Log.d(errorCode,t.getMessage());
                        }
                    });
                }else{
                    editTextEmail.setError("You need to enter your email address!");
                }
            }
        });

        super.onViewCreated(root, savedInstanceState);

    }

    private void proceedNext() {
        Bundle result = new Bundle();
        result.putString("fragment","email_fragment");
        result.putString("email",editTextEmail.getText().toString());
        getParentFragmentManager().setFragmentResult("signup_fragment",result);
    }


}
