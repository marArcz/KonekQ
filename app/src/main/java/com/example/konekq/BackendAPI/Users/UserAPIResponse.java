package com.example.konekq.BackendAPI.Users;

import com.example.konekq.BackendAPI.APIResponse;
import com.example.konekq.Models.User;

public class UserAPIResponse extends APIResponse {
    private User user;
    private String token;

    public UserAPIResponse(boolean success, int status_code, String message, User user, String token) {
        super(success, status_code, message);
        this.user = user;
        this.token = token;
    }

    public UserAPIResponse() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
