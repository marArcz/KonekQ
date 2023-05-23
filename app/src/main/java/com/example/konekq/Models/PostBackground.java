package com.example.konekq.Models;

import com.example.konekq.BackendAPI.RetrofitClient;

public class PostBackground {
    public static final int TEXT_WHITE = 1;
    public static final int TEXT_BLACK = 0;
    private int id;
    private String src;

    private int text_color = TEXT_BLACK;
    private boolean selected = false;

    public PostBackground(int id, String src, int text_color) {
        this.id = id;
        this.src = src;
        this.text_color = text_color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSrc() {
        return RetrofitClient.baseUrl + src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public int getText_color() {
        return text_color;
    }

    public void setText_color(int text_color) {
        this.text_color = text_color;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
