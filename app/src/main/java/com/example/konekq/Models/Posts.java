package com.example.konekq.Models;

import java.time.LocalDateTime;

public class Posts {
    private int id;
    private int userId;
    private String content;

    private LocalDateTime datePosted;

    public Posts(int id, int userId, String content) {
        this.id = id;
        this.userId = userId;
        this.content = content;
    }

    public Posts(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(LocalDateTime datePosted) {
        this.datePosted = datePosted;
    }
}
