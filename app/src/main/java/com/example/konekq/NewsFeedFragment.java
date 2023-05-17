package com.example.konekq;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.konekq.Models.PostPhoto;
import com.example.konekq.Models.Posts;
import com.example.konekq.Models.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFeedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewsFeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFeedFragment newInstance(String param1, String param2) {
        NewsFeedFragment fragment = new NewsFeedFragment();
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
        View root = inflater.inflate(R.layout.newsfeed_fragment_layout, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recycle_view);
        ArrayList<Posts> posts = new ArrayList<>();

        User user = new User();
        user.setFirstname("Belle");
        user.setFirstname("Soriano");

        PostPhoto[] photos = {
                new PostPhoto(0,0,"https://th.bing.com/th/id/OIP.V1iHyCRxOq-FLxKhMURGQQHaKk?pid=ImgDet&rs=1"),
                new PostPhoto(0,0,"https://images.alphacoders.com/112/thumb-1920-112121.jpg"),
                new PostPhoto(0,0,"https://th.bing.com/th/id/OIP.k8J4CZohC5RpLYxMvKKmZAHaEo?pid=ImgDet&rs=1")
        };
        posts.add(new Posts(user,0,0,"Hello Everyone!","https://images.alphacoders.com/112/thumb-1920-112121.jpg"));
        posts.add(new Posts(user,0,0,"Good Morning!"));
        posts.add(new Posts(user,0,0,"Good Afternoon!", "https://th.bing.com/th/id/OIP.V1iHyCRxOq-FLxKhMURGQQHaKk?pid=ImgDet&rs=1"));
        posts.add(new Posts(user,0,0,"Good Evening!","https://th.bing.com/th/id/OIP.k8J4CZohC5RpLYxMvKKmZAHaEo?pid=ImgDet&rs=1"));
        posts.add(new Posts(user,0,0,"Good Night!"));

        PostsRecyclerAdapter postsRecyclerAdapter = new PostsRecyclerAdapter(getContext(),posts);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(postsRecyclerAdapter);


        return root;
    }
}