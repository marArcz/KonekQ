package com.example.konekq.BackendAPI.Posts;

import com.example.konekq.BackendAPI.APIResponse;
import com.example.konekq.Models.Posts;

import java.util.ArrayList;

public class PostsAPIResponse extends APIResponse {
    private Posts post = null;
    private ArrayList<Posts> posts = null;

    public Posts getPost() {
        return post;
    }

    public void setPost(Posts post) {
        this.post = post;
    }

    public ArrayList<Posts> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Posts> posts) {
        this.posts = posts;
    }
}
