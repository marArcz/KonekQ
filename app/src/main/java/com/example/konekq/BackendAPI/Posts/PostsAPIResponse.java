package com.example.konekq.BackendAPI.Posts;

import com.example.konekq.BackendAPI.APIResponse;
import com.example.konekq.Models.Posts;

import java.util.ArrayList;

public class PostsAPIResponse extends APIResponse {
    private Posts Post = null;
    private ArrayList<Posts> posts = null;

    public Posts getPost() {
        return Post;
    }

    public void setPost(Posts post) {
        Post = post;
    }

    public ArrayList<Posts> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Posts> posts) {
        this.posts = posts;
    }
}
