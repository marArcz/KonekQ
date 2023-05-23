package com.example.konekq.BackendAPI.Posts;

import com.example.konekq.BackendAPI.APIResponse;
import com.example.konekq.Models.PostBackground;

import java.util.ArrayList;

public class PostBackgroundAPIResponse extends APIResponse {
    private ArrayList<PostBackground> backgrounds;

    public ArrayList<PostBackground> getBackgrounds() {
        return backgrounds;
    }

    public void setBackgrounds(ArrayList<PostBackground> backgrounds) {
        this.backgrounds = backgrounds;
    }
}
