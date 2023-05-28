package com.example.konekq;

import static android.app.Activity.RESULT_OK;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.konekq.BackendAPI.Posts.PostsAPIResponse;
import com.example.konekq.BackendAPI.Posts.PostsAPIService;
import com.example.konekq.BackendAPI.RetrofitClient;
import com.example.konekq.Models.Posts;
import com.example.konekq.Models.User;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    ArrayList<Posts> posts;
    PostsRecyclerAdapter postsRecyclerAdapter;
    RecyclerView recyclerView;
    DataChangedListener dataChangedListener;
    CustomProgressDialog progressDialog;

    public UserProfileFragment(DataChangedListener dataChangedListener) {
        // Required empty public constructor
        this.dataChangedListener = dataChangedListener;
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
        UserProfileFragment fragment = new UserProfileFragment(null);
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
                    if (result.getResultCode() == RESULT_OK) {
                        progressDialog.show();
                        Glide.with(getContext()).load(AppManager.getUser(getContext()).getProfile_photo()).placeholder(R.drawable.gray).into(imageViewProfile);
                        loadPosts(new RequestCompleteListener<Posts>() {
                            @Override
                            public void onComplete(@Nullable Posts data) {
                                progressDialog.dismiss();
                            }
                        });
                    }
                }
            });

    ActivityResultLauncher<Intent> launcher=
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                if(result.getResultCode()==RESULT_OK){
                    Uri uri=result.getData().getData();
                    if(uri != null){
                        Intent intent = new Intent(getActivity(),ChangeProfilePicActivity.class);
                        intent.putExtra("photo",uri);
                        mStartForResult.launch(intent);
                    }
                }else if(result.getResultCode()== ImagePicker.RESULT_ERROR){

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
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        textViewName = view.findViewById(R.id.user_name);
        imageViewProfile = view.findViewById(R.id.profile_photo);
        imageViewCover = view.findViewById(R.id.cover_photo);
        progressDialog = new CustomProgressDialog(getContext());
        recyclerView = view.findViewById(R.id.posts_recycler_view);
        posts = new ArrayList<>();

        user = AppManager.getUser(getContext());

        textViewName.setText(String.format("%s %s", user.getFirstname(),user.getLastname()));
        Glide.with(getContext()).load(user.getProfile_photo()).into(imageViewProfile);
        Glide.with(getContext()).load(user.getCover_photo()).into(imageViewCover);

        loadPosts(new RequestCompleteListener<Posts>(){
            @Override
            public void onComplete(@Nullable Posts data) {

            }
        });

        postsRecyclerAdapter = new PostsRecyclerAdapter(getContext(), posts, new PostsRecyclerAdapter.DataChangedListener() {
            @Override
            public void OnChanged(ArrayList<Posts> posts) {
                posts = posts;
                if(dataChangedListener != null) dataChangedListener.onDataSetChanged(posts);

            }
        }, new PostsRecyclerAdapter.PostClickedListener() {
            @Override
            public void onClicked(Posts post) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(postsRecyclerAdapter);


        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter adapter = new ArrayAdapter(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,new String[]{"Change Photo","View Photo"});
                new AlertDialog.Builder(getContext())
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               switch (which){
                                   case 0:
                                       ImagePicker.Companion.with(getActivity())
                                               .crop()
                                               .cropOval()
                                               .maxResultSize(512,512,true)
                                               .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                                               .createIntentFromDialog(new Function1<Intent, Unit>() {
                                                   @Override
                                                   public Unit invoke(Intent intent) {
                                                       launcher.launch(intent);
                                                       return null;
                                                   }
                                               });
                                       break;
                                   case 1:
                                       dialog.dismiss();
                                       Intent intent = new Intent(getActivity(), ViewPhotoActivity.class);
                                       intent.putExtra("image",user.getProfile_photo());
                                       startActivity(intent);
                                       break;
                               }
                            }
                        })
                        .show();
            }
        });

        return view;
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

    private void loadPosts(RequestCompleteListener<Posts> postsRequestCompleteListener) {
        String token = AppManager.getToken(getContext());
        String errorCode = ErrorCodes.GETTING_OWN_POSTS;
        Call<PostsAPIResponse> getOwnPosts = RetrofitClient.getPostService(token).getOwnPosts();
        getOwnPosts.enqueue(new Callback<PostsAPIResponse>() {
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

                if(postsRequestCompleteListener != null) postsRequestCompleteListener.onComplete(null);
            }
            @Override
            public void onFailure(Call<PostsAPIResponse> call, Throwable t) {
                if(postsRequestCompleteListener != null) postsRequestCompleteListener.onComplete(null);
                new CustomAlertDialog(getContext())
                        .showError(errorCode);
                Log.d(errorCode,t.getMessage());
            }
        });

    }
}