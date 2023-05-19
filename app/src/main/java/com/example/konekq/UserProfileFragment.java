package com.example.konekq;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.konekq.Models.Posts;
import com.example.konekq.Models.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {
    User user;
    ImageView imageViewProfile, imageViewCover;
    TextView textViewName;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        textViewName = view.findViewById(R.id.user_name);
        imageViewProfile = view.findViewById(R.id.profile_photo);
        imageViewCover = view.findViewById(R.id.cover_photo);

        RecyclerView recyclerView = view.findViewById(R.id.posts_recycler_view);
        ArrayList<Posts> posts = new ArrayList<>();
        user = AppManager.getUser(getContext());

        textViewName.setText(String.format("%s %s", user.getFirstname(),user.getLastname()));
        Glide.with(getContext()).load(user.getProfile_photo()).into(imageViewProfile);
        Glide.with(getContext()).load(user.getCover_photo()).into(imageViewCover);

        posts.add(new Posts(user,0,0,"Hello Everyone!"));
        posts.add(new Posts(user,0,0,"Good Morning!"));
        posts.add(new Posts(user,0,0,"Good Afternoon!"));
        posts.add(new Posts(user,0,0,"Good Evening!"));
        posts.add(new Posts(user,0,0,"Good Night!"));

        PostsRecyclerAdapter postsRecyclerAdapter = new PostsRecyclerAdapter(getContext(),posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(postsRecyclerAdapter);
        return view;
    }
}