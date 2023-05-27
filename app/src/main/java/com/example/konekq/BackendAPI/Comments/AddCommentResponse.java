package com.example.konekq.BackendAPI.Comments;

import com.example.konekq.BackendAPI.APIResponse;
import com.example.konekq.Models.Comment;

public class AddCommentResponse extends APIResponse {
    private Comment comment;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
