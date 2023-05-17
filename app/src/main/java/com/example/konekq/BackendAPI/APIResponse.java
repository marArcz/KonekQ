package com.example.konekq.BackendAPI;

public class APIResponse {
    private boolean success;
    private int status_code;
    private String message;


    public APIResponse() {
    }

    public APIResponse(boolean success, int status_code, String message) {
        this.success = success;
        this.status_code = status_code;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
