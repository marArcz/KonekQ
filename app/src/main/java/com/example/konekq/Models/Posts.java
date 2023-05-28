package com.example.konekq.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.konekq.BackendAPI.RetrofitClient;

public class Posts implements Parcelable {

    public static final int TYPE_CAPTION_ONLY = 0;
    public static final int TYPE_CAPTION_WITH_PHOTOS = 1;
    private int id;
    private int user_id;
    private String content;
    private String photo;

    private String created_at;
    private int type = TYPE_CAPTION_ONLY;
    private boolean liked;

    private User user = null;

    private PostBackground background = null;
    private int likes = 0;
    private int comments = 0;
    private String time_fetched;
    private boolean owned;

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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
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
        return RetrofitClient.baseUrl + photo;
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

    public PostBackground getBackground() {
        return background;
    }

    public void setBackground(PostBackground background) {
        this.background = background;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getTime_fetched() {
        return time_fetched;
    }

    public boolean isOwned() {
        return owned;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public void setTime_fetched(String time_fetched) {
        this.time_fetched = time_fetched;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}
