package com.example.konekq.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Posts {

    public static final int TYPE_CAPTION_ONLY = 0;
    public static final int TYPE_CAPTION_WITH_PHOTOS = 1;
    private int id;
    private int user_id;
    private String content;
    private String photo;

    private LocalDateTime date_posted;
    private int type = TYPE_CAPTION_ONLY;
    private boolean liked;

    private User user = null;

    public Posts(User user, int id, int userId, String content) {
        this.user = user;
        this.id = id;
        this.user_id = userId;
        this.content = content;
        this.type = TYPE_CAPTION_ONLY;
    }

    public Posts(User user, int id, int userId, String content, String photo) {
        this.user = user;
        this.id = id;
        this.user_id = userId;
        this.content = content;
        this.type = TYPE_CAPTION_WITH_PHOTOS;
        this.photo = photo;
    }

    public Posts(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDate_posted() {
        return date_posted;
    }

    public void setDate_posted(LocalDateTime date_posted) {
        this.date_posted = date_posted;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
