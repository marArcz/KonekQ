package com.example.konekq.BackendAPI.Comments;

import com.example.konekq.BackendAPI.APIResponse;
import com.example.konekq.Models.Comment;

import java.util.ArrayList;

public class CommentsAPIResponse extends APIResponse {
    private ArrayList<Comment> comments;

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}
