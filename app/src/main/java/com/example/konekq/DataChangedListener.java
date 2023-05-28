package com.example.konekq;

import com.example.konekq.Models.Posts;

import java.util.ArrayList;

public interface DataChangedListener{
    void onItemChanged(int index, Posts posts);
    void onDataSetChanged(ArrayList<Posts> newPosts);
    void onDataUpdateNeeded();
}
