package com.example.konekq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.konekq.Models.PostBackground;

import java.util.ArrayList;

public class PostBgRecyclerAdapter extends RecyclerView.Adapter<PostBgRecyclerAdapter.PostBgViewHolder> {
    public interface OnItemSelectedListener{
        void onSelect(PostBackground postBg,int position);
    }
    ArrayList<PostBackground> backgrounds;
    Context context;
    OnItemSelectedListener onItemSelectedListener;

    public PostBgRecyclerAdapter(Context context, ArrayList<PostBackground> backgrounds) {
        this.backgrounds = backgrounds;
        this.context = context;
    }

    public PostBgRecyclerAdapter(Context context,ArrayList<PostBackground> backgrounds, OnItemSelectedListener onItemSelectedListener) {
        this.backgrounds = backgrounds;
        this.context = context;
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @NonNull
    @Override
    public PostBgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostBgViewHolder(LayoutInflater.from(context).inflate(R.layout.post_bg_list_layout,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostBgViewHolder holder, int position) {
        int index = position;
        if(position == 0){
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.post_bg_none));
            holder.imageView.setElevation(1f);
        }else{
            if(backgrounds.get(position).isSelected()){
                holder.viewIndicator.setVisibility(View.VISIBLE);
            }
            Glide.with(context).load(backgrounds.get(position).getSrc()).into(holder.imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemSelectedListener != null){
                    onItemSelectedListener.onSelect(backgrounds.get(index),index);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return backgrounds.size();
    }

    public class PostBgViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        View viewIndicator;
        public PostBgViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            viewIndicator = itemView.findViewById(R.id.view_indicator);
        }
    }
}
