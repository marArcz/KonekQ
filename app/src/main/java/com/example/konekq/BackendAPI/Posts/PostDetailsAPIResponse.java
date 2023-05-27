package com.example.konekq.BackendAPI.Posts;

import com.example.konekq.BackendAPI.APIResponse;
import com.example.konekq.Models.Posts;

public class PostDetailsAPIResponse extends APIResponse {
    private Posts post = null;

    public Posts getPost() {
        return post;
    }

    public void setPost(Posts post) {
        this.post = post;
    }
}
