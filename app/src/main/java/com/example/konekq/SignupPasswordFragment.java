package com.example.konekq;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupPasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignupPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupPasswordFragment newInstance(String param1, String param2) {
        SignupPasswordFragment fragment = new SignupPasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_signup_password, container, false);
        Button btnNext = root.findViewById(R.id.btn_next);
        EditText editTextPassword = root.findViewById(R.id.edit_text_password);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editTextPassword.getText().toString();

                if(password.isBlank() || password.length() < 6){
                    editTextPassword.setError("Password should have at least 6 characters!");
                }else{
                    Bundle result = new Bundle();
                    result.putString("fragment","password_fragment");
                    result.putString("password",password);
                    getParentFragmentManager().setFragmentResult("signup_fragment",result);
                }
            }
        });

        return root;
    }
}