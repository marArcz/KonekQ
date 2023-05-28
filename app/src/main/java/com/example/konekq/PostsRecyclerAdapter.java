package com.example.konekq;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.konekq.BackendAPI.APIResponse;
import com.example.konekq.BackendAPI.RetrofitClient;
import com.example.konekq.Models.Comment;
import com.example.konekq.Models.PostBackground;
import com.example.konekq.Models.Posts;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private interface RequestCompleteListener{
        void onComplete(Posts newPosts);
    }

    public static interface DataChangedListener{
        void OnChanged(ArrayList<Posts> posts);
    }
    public static interface PostClickedListener{
        void onClicked(Posts post);
    }

    DataChangedListener dataChangedListener;
    private final int VIEW_TYPE_LOADING = 100;
    private final Context context;
    ArrayList<Posts> posts;
    private int viewedPostId;
    PostClickedListener postClickedListener;


    public PostsRecyclerAdapter(Context context,ArrayList<Posts> posts, DataChangedListener dataChangedListener, PostClickedListener postClickedListener) {
        this.context = context;
        this.posts = posts;

        this.dataChangedListener = dataChangedListener;
        this.postClickedListener = postClickedListener;
    }


    @Override
    public int getItemViewType(int position) {
        return posts.get(position) == null ? VIEW_TYPE_LOADING : posts.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_loading_layout,parent,false);
            return new LoadingViewHolder(view);
        }
        else{
            View view;
            if(viewType == Posts.TYPE_CAPTION_ONLY){
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_caption_only_layout, parent, false);
                return new PostCaptionOnlyViewHolder(view);
            }else{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_caption_with_photos_layout, parent, false);
                return new PostWithPhotosViewHolder(view);
            }

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        holder.textViewCaption.setText(posts.get(position).getContent());
        if(holder instanceof LoadingViewHolder){
            showLoadingView(((LoadingViewHolder) holder),position);
        }else{
            addItemView(holder,position);
        }
    }



    @Override
    public int getItemCount() {
        return posts.size();
    }


    public void addItemView(RecyclerView.ViewHolder holder, int position){
        Posts post = posts.get(position);
        if(holder instanceof PostCaptionOnlyViewHolder){
            PostCaptionOnlyViewHolder viewHolder = (PostCaptionOnlyViewHolder) holder;
            Glide.with(context).load(post.getUser().getProfile_photo()).into(viewHolder.imageViewProfile);
            viewHolder.textViewCaption.setText(post.getContent());
            if(post.getLikes() > 0){
                viewHolder.textViewLikes.setText(post.getLikes() > 1000? String.valueOf(post.getLikes()).charAt(0)+"k": String.valueOf(post.getLikes()));
            }else{
                viewHolder.textViewLikes.setText("");
            }
            if(post.getCreated_at() != null && !post.getCreated_at().isBlank() && (post.getTime_fetched() != null)){
                Timestamp timestamp = Timestamp.valueOf(post.getCreated_at());
                String timePassed =
                        String.valueOf(DateUtils.getRelativeTimeSpanString(timestamp.getTime(), Timestamp.valueOf(post.getTime_fetched()).getTime(), 0));
                viewHolder.textViewDateTime.setText(timePassed);

            }

            if(post.isOwned()){
                viewHolder.btnOptions.setVisibility(View.VISIBLE);
                viewHolder.btnOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBottomSheetDialog(post);
                    }
                });
            }

            viewHolder.btnComment.setText(String.valueOf(post.getComments()));
            viewHolder.btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(postClickedListener != null) postClickedListener.onClicked(post);
                }
            });
            if(!post.isLiked()){
                viewHolder.btnLike.setText("");
                viewHolder.btnLike.setIcon(context.getDrawable(R.drawable.outline_favorite_border_24));
            }else{
                if(post.getLikes() > 0){
                    viewHolder.btnLike.setText(post.getLikes() > 1000? String.valueOf(post.getLikes()).charAt(0)+"k": String.valueOf(post.getLikes()));
                }else{
                    viewHolder.btnLike.setText("");
                }
            }
            viewHolder.postHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(postClickedListener != null) postClickedListener.onClicked(post);
                }
            });
            viewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!post.isLiked()){
                        viewHolder.btnLike.setIcon(context.getDrawable(R.drawable.baseline_favorite_24));
                    }else{
                        viewHolder.btnLike.setIcon(context.getDrawable(R.drawable.outline_favorite_border_24));
                    }
                    likePost(post, new RequestCompleteListener() {
                        @Override
                        public void onComplete(Posts newPosts) {
                            posts.set(posts.indexOf(post),newPosts);
                            notifyItemChanged(posts.indexOf(post));
                            if(dataChangedListener != null) dataChangedListener.OnChanged(posts);
                        }
                    });
                }
            });

            if(post.getBackground() != null){
                Glide.with(context).load(post.getBackground().getSrc())
                        .into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                viewHolder.postCaptionLayout.setBackground(resource);
                                viewHolder.textViewCaption.setTextColor(post.getBackground().getText_color() == PostBackground.TEXT_BLACK? Color.BLACK:Color.WHITE);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            }
            viewHolder.textViewName.setText(String.format("%s %s", post.getUser().getFirstname(),post.getUser().getLastname()));

        }else{
            PostWithPhotosViewHolder viewHolder = (PostWithPhotosViewHolder) holder;
            Glide.with(context).load(post.getUser().getProfile_photo()).into(viewHolder.imageViewProfile);
            viewHolder.textViewName.setText(String.format("%s %s", post.getUser().getFirstname(),post.getUser().getLastname()));
            viewHolder.textViewCaption.setText(post.getContent());
            if(post.getLikes() > 0){
                viewHolder.textViewLikes.setText(post.getLikes() > 1000? String.valueOf(post.getLikes()).charAt(0)+"k": String.valueOf(post.getLikes()));
            }else{
                viewHolder.textViewLikes.setText("");
            }
            if(post.getCreated_at() != null && !post.getCreated_at().isBlank() && (post.getTime_fetched() != null)){
                Timestamp timestamp = Timestamp.valueOf(post.getCreated_at());
                String timePassed =
                        String.valueOf(DateUtils.getRelativeTimeSpanString(timestamp.getTime(), Timestamp.valueOf(post.getTime_fetched()).getTime(), 0));
                viewHolder.textViewDateTime.setText(timePassed);
            }
            if(post.isOwned()){
                viewHolder.btnOptions.setVisibility(View.VISIBLE);
                viewHolder.btnOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBottomSheetDialog(post);
                    }
                });
            }

            viewHolder.btnComment.setText(String.valueOf(post.getComments()));

            viewHolder.btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(postClickedListener != null) postClickedListener.onClicked(post);
                }
            });
            if(!post.isLiked()){
                viewHolder.btnLike.setText("");
                viewHolder.btnLike.setIcon(context.getDrawable(R.drawable.outline_favorite_border_24));
            }else{
                if(post.getLikes() > 0){
                    viewHolder.btnLike.setText(post.getLikes() > 1000? String.valueOf(post.getLikes()).charAt(0)+"k": String.valueOf(post.getLikes()));
                }else{
                    viewHolder.btnLike.setText("");
                }

            }
            viewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!post.isLiked()){
                        viewHolder.btnLike.setIcon(context.getDrawable(R.drawable.baseline_favorite_24));
                    }else{
                        viewHolder.btnLike.setIcon(context.getDrawable(R.drawable.outline_favorite_border_24));
                    }
                    likePost(post, new RequestCompleteListener() {
                        @Override
                        public void onComplete(Posts newPost) {
                            posts.set(posts.indexOf(post),newPost);
                            notifyItemChanged(posts.indexOf(post));
                            if(dataChangedListener != null) dataChangedListener.OnChanged(posts);
                        }
                    });
                }
            });
            Glide.with(context).load(post.getPhoto()).into(viewHolder.imageView);
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = (ImageView) v;
                    Intent intent = new Intent(context,ViewPhotoActivity.class);
                    intent.putExtra("image",post.getPhoto());

                    context.startActivity(intent);
                }
            });
            viewHolder.postHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(postClickedListener != null) postClickedListener.onClicked(post);
                }
            });
        }
    }

    private void showBottomSheetDialog(Posts post) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view =
                LayoutInflater.from(context).inflate(R.layout.comments_bottom_sheet_layout, null, false);
        bottomSheetDialog.setContentView(view);
        ArrayList<BottomSheetMenuAdapter.Option> options = new ArrayList<>();
        options.add(new BottomSheetMenuAdapter.Option(R.drawable.baseline_edit_dark_12, "Edit Post"));
        options.add(new BottomSheetMenuAdapter.Option(R.drawable.baseline_delete_24_dark, "Delete Post"));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        BottomSheetMenuAdapter adapter =
                new BottomSheetMenuAdapter(options, context, new BottomSheetMenuAdapter.ItemClickedListener() {
                    @Override
                    public void onClick(BottomSheetMenuAdapter.Option option, int position) {
                        switch (position) {
                            case 0:
                                break;
                            case 1:
                                bottomSheetDialog.dismiss();
                                deletePost(post);
                                break;
                        }
                    }
                });
        recyclerView.setAdapter(adapter);

        bottomSheetDialog.show();
    }

    private void deletePost(Posts post) {
        String token = AppManager.getToken(context);
        Call<APIResponse> deletePost = RetrofitClient.getPostService(token).deletePost(post.getId());
        deletePost.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(response.body() == null){
                    Toast.makeText(context, "Server response is empty!", Toast.LENGTH_SHORT).show();
                }else{
                 if(response.body().isSuccess()){
                     int index = posts.indexOf(post);
                     posts.remove(index);
                     notifyItemRemoved(index);
                     Toast.makeText(context, "Post was successfully removed!", Toast.LENGTH_SHORT).show();
                 }else{
                     new CustomAlertDialog(context)
                             .setMessage(response.body().getMessage())
                             .showError();
                 }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                new CustomAlertDialog(context)
                        .setMessage(t.getMessage())
                        .showError();
            }
        });
    }

    private void showLoadingView(LoadingViewHolder loadingViewHolder, int position){

    }

    private void likePost(Posts posts, RequestCompleteListener requestCompleteListener){
        if(posts.isLiked()){
            removeLike(posts, requestCompleteListener);
        }else{
            addLike(posts, requestCompleteListener);
        }
    }

    public void addLike(Posts posts, RequestCompleteListener requestCompleteListener){
        String token = AppManager.getToken(context);
        Call<APIResponse> likePostCall = RetrofitClient.getPostService(token).likePost(posts.getId());
        String errorCode = ErrorCodes.LIKE_POST;
        likePostCall.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(response.body() == null){
                    Toast.makeText(context, "Server response is empty!", Toast.LENGTH_SHORT).show();
                }else{
                    if(response.body().isSuccess()){
                        Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show();
                        posts.setLikes(posts.getLikes()+1);
                        posts.setLiked(true);
                    }else{
                        new CustomAlertDialog(context)
                                .setMessage(response.body().getMessage())
                                .showError();
                    }
                }
                if(requestCompleteListener != null) requestCompleteListener.onComplete(posts);
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                new CustomAlertDialog(context)
                        .showError(errorCode);
                Log.d(errorCode,t.getMessage());
                if(requestCompleteListener != null) requestCompleteListener.onComplete(posts);
            }
        });
    }
    public void removeLike(Posts posts, RequestCompleteListener requestCompleteListener){
        String token = AppManager.getToken(context);
        Call<APIResponse> unlikePostCall = RetrofitClient.getPostService(token).unlikePost(posts.getId());
        String errorCode = ErrorCodes.LIKE_POST;
        unlikePostCall.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(response.body() == null){
                    Toast.makeText(context, "Server response is empty!", Toast.LENGTH_SHORT).show();
                }else{
                    if(response.body().isSuccess()){
                        Toast.makeText(context, "UnLiked", Toast.LENGTH_SHORT).show();
                        posts.setLikes(posts.getLikes()-1);
                        posts.setLiked(false);
                    }else{
                        new CustomAlertDialog(context)
                                .setMessage(response.body().getMessage())
                                .showError();
                    }
                }

                if(requestCompleteListener != null) requestCompleteListener.onComplete(posts);
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                new CustomAlertDialog(context)
                        .showError(errorCode);
                Log.d(errorCode,t.getMessage());
                if(requestCompleteListener != null) requestCompleteListener.onComplete(posts);
            }
        });
    }


    //Item view holder
    public class PostCaptionOnlyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewCaption, textViewDateTime;
        TextView textViewName,textViewLikes;
        ImageView imageViewProfile;
        LinearLayout postCaptionLayout;
        MaterialButton btnLike,btnComment;
        RelativeLayout postHeader;
        MaterialButton btnOptions;

        public PostCaptionOnlyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textview_name);
            textViewCaption = itemView.findViewById(R.id.textView_caption);
            textViewDateTime = itemView.findViewById(R.id.textview_date_time);
            imageViewProfile = itemView.findViewById(R.id.profile_photo);
            postCaptionLayout = itemView.findViewById(R.id.post_caption_layout);
            btnComment = itemView.findViewById(R.id.btn_comment);
            btnLike = itemView.findViewById(R.id.btn_like);
            textViewLikes = itemView.findViewById(R.id.textView_likes);
            postHeader = itemView.findViewById(R.id.post_header);
            btnOptions = itemView.findViewById(R.id.btn_options);

        }
    }

    public class PostWithPhotosViewHolder extends RecyclerView.ViewHolder{
        TextView textViewCaption,textViewName,textViewDateTime,textViewLikes;
        ImageView imageView;
        ImageView imageViewProfile;
        MaterialButton btnLike,btnComment;
        RelativeLayout postHeader;
        MaterialButton btnOptions;


        public PostWithPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textview_name);
            textViewCaption = itemView.findViewById(R.id.textView_caption);
            imageView = itemView.findViewById(R.id.imageView);
            textViewDateTime = itemView.findViewById(R.id.textview_date_time);
            imageViewProfile = itemView.findViewById(R.id.profile_photo);
            btnComment = itemView.findViewById(R.id.btn_comment);
            btnLike = itemView.findViewById(R.id.btn_like);
            textViewLikes = itemView.findViewById(R.id.textView_likes);
            postHeader = itemView.findViewById(R.id.post_header);
            btnOptions = itemView.findViewById(R.id.btn_options);


        }
    }


    //Loading View holder
    public class LoadingViewHolder extends RecyclerView.ViewHolder{

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
