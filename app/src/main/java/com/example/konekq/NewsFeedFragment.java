package com.example.konekq;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.konekq.BackendAPI.Posts.PostsAPIResponse;
import com.example.konekq.BackendAPI.RetrofitClient;
import com.example.konekq.Models.PostPhoto;
import com.example.konekq.Models.Posts;
import com.example.konekq.Models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFeedFragment extends Fragment {
    private interface PostsLoadedListener{
        void onLoaded();
    }
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Posts> posts;
    RecyclerView recyclerView;
    ImageView imageViewProfile;
    PostsRecyclerAdapter postsRecyclerAdapter;
    Button btnCreatePost;
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
        imageViewProfile = root.findViewById(R.id.profile_photo);
        btnCreatePost = root.findViewById(R.id.btn_create_post);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPosts(new PostsLoadedListener() {
                    @Override
                    public void onLoaded() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AddPostActivity.class));
            }
        });

        recyclerView = root.findViewById(R.id.recycle_view);
        posts = new ArrayList<>();

        User user = AppManager.getUser(getContext());

        Glide.with(getContext()).load(user.getProfile_photo()).into(imageViewProfile);

//        posts.add(new Posts(user,0,0,"Hello Everyone!","https://images.alphacoders.com/112/thumb-1920-112121.jpg"));
//        posts.add(new Posts(user,0,0,"Good Morning!"));
//        posts.add(new Posts(user,0,0,"Good Afternoon!", "https://th.bing.com/th/id/OIP.V1iHyCRxOq-FLxKhMURGQQHaKk?pid=ImgDet&rs=1"));
//        posts.add(new Posts(user,0,0,"Good Evening!","https://th.bing.com/th/id/OIP.k8J4CZohC5RpLYxMvKKmZAHaEo?pid=ImgDet&rs=1"));
//        posts.add(new Posts(user,0,0,"Good Night!"));

        postsRecyclerAdapter = new PostsRecyclerAdapter(getContext(),posts);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(postsRecyclerAdapter);
        loadPosts(null);

        return root;
    }

    private void loadPosts(PostsLoadedListener postsLoadedListener){
        String token = AppManager.getToken(getContext());
        Call<PostsAPIResponse> getPosts = RetrofitClient.getPostService(token).getAll();
        String errorCode = ErrorCodes.GETTING_POSTS;
        getPosts.enqueue(new Callback<PostsAPIResponse>() {
            @Override
            public void onResponse(Call<PostsAPIResponse> call, Response<PostsAPIResponse> response) {
                if(response.body() == null){
                    Toast.makeText(getContext(), "Server response is empty", Toast.LENGTH_SHORT).show();
                }else{
                    if(response.body().isSuccess()){
                        posts = response.body().getPosts();
                        postsRecyclerAdapter = new PostsRecyclerAdapter(getContext(),posts);
                        recyclerView.setAdapter(postsRecyclerAdapter);
                        Toast.makeText(getContext(), "Successfully loaded!", Toast.LENGTH_SHORT).show();
                    }else{
                        new CustomAlertDialog(getContext())
                                .setMessage(response.body().getMessage())
                                .showError();
                    }
                }

                if(postsLoadedListener != null)postsLoadedListener.onLoaded();
            }
            @Override
            public void onFailure(Call<PostsAPIResponse> call, Throwable t) {
                if(postsLoadedListener != null)postsLoadedListener.onLoaded();
                new CustomAlertDialog(getContext())
                        .showError(errorCode);
                Log.d(errorCode,t.getMessage());
            }
        });

    }
}