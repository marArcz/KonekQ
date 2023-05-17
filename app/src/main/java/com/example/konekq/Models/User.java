package com.example.konekq.Models;

public class User {
    public static final int DELETED_TRUE = 1;
    public static final int DELETED_FALSE = 0;

    private int id;
    private String firstname;
    private String lastname;
    private String email_address;
    private String gender;
    private String profile_photo;
    private String cover_photo;
    private String password;
    private String birthday;
    private int is_deleted = DELETED_FALSE;

    public User() {
    }

    public User(int id, String firstname, String lastname, String email_address, String gender, String profile_photo, String cover_photo, String password, String birthday, int is_deleted) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email_address = email_address;
        this.gender = gender;
        this.profile_photo = profile_photo;
        this.cover_photo = cover_photo;
        this.password = password;
        this.birthday = birthday;
        this.is_deleted = is_deleted;
    }

    public User(String firstname, String lastname, String email_address, String gender, String profile_photo, String cover_photo, String password, String birthday) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email_address = email_address;
        this.gender = gender;
        this.profile_photo = profile_photo;
        this.cover_photo = cover_photo;
        this.password = password;
        this.birthday = birthday;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getCover_photo() {
        return cover_photo;
    }

    public void setCover_photo(String cover_photo) {
        this.cover_photo = cover_photo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }
}
