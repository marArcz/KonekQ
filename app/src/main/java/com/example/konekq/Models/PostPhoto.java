package com.example.konekq.Models;

public class PostPhoto {
    private int id;
    private String src;
    private int post_id;

    public PostPhoto() {
    }

    public PostPhoto(int id, int post_id,String src) {
        this.id = id;
        this.src = src;
        this.post_id = post_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
}
