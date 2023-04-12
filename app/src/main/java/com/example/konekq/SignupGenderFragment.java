package com.example.konekq;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ListMenuItemView;
import androidx.fragment.app.Fragment;

public class SignupGenderFragment extends Fragment {
    public SignupGenderFragment(){
        super(R.layout.signup_gender_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        View root = view;
        ListView listView = root.findViewById(R.id.gender_listview);
        String genders[] = {"Female","Male","Custom"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item,genders);
        listView.setAdapter(arrayAdapter);
        super.onViewCreated(root, savedInstanceState);
    }
}
