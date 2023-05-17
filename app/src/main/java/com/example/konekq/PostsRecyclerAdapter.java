package com.example.konekq;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.konekq.Models.PostPhoto;
import com.example.konekq.Models.Posts;

import java.util.ArrayList;

public class PostsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_LOADING = 100;
    private Context context;
    ArrayList<Posts> posts;

    public PostsRecyclerAdapter(Context context,ArrayList<Posts> posts) {
        this.context = context;
        this.posts = posts;
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
            viewHolder.textViewCaption.setText(post.getContent());

        }else{
            PostWithPhotosViewHolder viewHolder = (PostWithPhotosViewHolder) holder;

            viewHolder.textViewCaption.setText(post.getContent());

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
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,ViewPostActivity.class));
                }
            });
        }
    }

    private void showLoadingView(LoadingViewHolder loadingViewHolder, int position){

    }


    //Item view holder
    public class PostCaptionOnlyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewCaption;
        public PostCaptionOnlyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewCaption = itemView.findViewById(R.id.textView_caption);
        }
    }

    public class PostWithPhotosViewHolder extends  RecyclerView.ViewHolder{
        TextView textViewCaption;
        ImageView imageView;
        public PostWithPhotosViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewCaption = itemView.findViewById(R.id.textView_caption);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }


    //Loading View holder
    public class LoadingViewHolder extends RecyclerView.ViewHolder{

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
