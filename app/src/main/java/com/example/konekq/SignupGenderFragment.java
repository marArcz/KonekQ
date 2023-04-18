package com.example.konekq;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ListMenuItemView;
import androidx.fragment.app.Fragment;

public class SignupGenderFragment extends Fragment {
    private int selectedIndex = 0;
    public SignupGenderFragment(){
        super(R.layout.signup_gender_fragment);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        View root = view;
        ListView listView = root.findViewById(R.id.gender_listview);
        String genders[] = {"Female","Male","Custom"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.select_dialog_singlechoice,genders);
        listView.setAdapter(arrayAdapter);
        Button btnNext =  root.findViewById(R.id.btn_next);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedIndex = position;
                btnNext.setEnabled(true);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putString("fragment","gender_fragment");
                result.putString("gender",genders[selectedIndex]);
                getParentFragmentManager().setFragmentResult("signup_fragment",result);
            }
        });

        super.onViewCreated(root, savedInstanceState);
    }
}
