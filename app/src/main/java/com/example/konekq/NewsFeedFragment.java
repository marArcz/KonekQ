package com.example.konekq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
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
import com.google.gson.Gson;

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


    DataChangedListener dataChangedListener;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Posts> posts;
    RecyclerView recyclerView;
    PostsRecyclerAdapter postsRecyclerAdapter;
    ImageView imageViewProfile;
    Button btnCreatePost;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public NewsFeedFragment(DataChangedListener dataChangedListener) {
        // Required empty public constructor
        this.dataChangedListener = dataChangedListener;
    }

    public static final int VIEW_POST_REQUEST_CODE = 122;
    public static final int ADD_POST_REQUEST_CODE = 123;

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
        NewsFeedFragment fragment = new NewsFeedFragment(null);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == VIEW_POST_REQUEST_CODE) {
                        Intent intent = result.getData();
                        int postId = intent.getIntExtra("postId",0);
                        int index = -1;
                        for(int x=0;x<posts.size();x++){
                            if(posts.get(x).getId() == postId){
                                index = x;
                                break;
                            }
                        }

                        if(index >= 0){
                            Gson gson = new Gson();
                            Posts post = gson.fromJson(intent.getStringExtra("post"),Posts.class);
                            posts.set(index,post);
                            postsRecyclerAdapter.notifyItemChanged(index);
                            if(dataChangedListener != null) dataChangedListener.onItemChanged(index,post);
                        }
                    }else if(result.getResultCode() == ADD_POST_REQUEST_CODE){
                        if(dataChangedListener != null) dataChangedListener.onDataUpdateNeeded();
                    }
                }
            });
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
                        if(dataChangedListener != null) dataChangedListener.onDataSetChanged(posts);
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

        postsRecyclerAdapter = new PostsRecyclerAdapter(getContext(), posts, new PostsRecyclerAdapter.DataChangedListener() {
            @Override
            public void OnChanged(ArrayList<Posts> newPosts) {
                posts = newPosts;
                if(dataChangedListener != null) dataChangedListener.onDataSetChanged(newPosts);
            }
        }, new PostsRecyclerAdapter.PostClickedListener() {
            @Override
            public void onClicked(Posts post) {
                Intent intent = new Intent(getActivity(),ViewPostActivity.class);
                intent.putExtra("post_id",post.getId());

                mStartForResult.launch(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(postsRecyclerAdapter);
        loadPosts(null);

        return root;
    }

    public ArrayList<Posts> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Posts> posts) {
        this.posts.clear();
        this.posts.addAll(posts);
        postsRecyclerAdapter.notifyDataSetChanged();

    }
    public void refreshPosts(){
        this.loadPosts(null);
    }
    public void loadPosts(PostsLoadedListener postsLoadedListener){
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
                        posts.clear();
                        posts.addAll(response.body().getPosts());
                        postsRecyclerAdapter.notifyDataSetChanged();
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