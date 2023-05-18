package com.example.konekq;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.konekq.Models.User;
import com.google.gson.Gson;

public class AppManager {
    public static final String SHARED_PREFERENCES_NAME = "APP_PREFERENCES_KONEKQ";

    public static boolean saveUser(User user, Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();

        Gson gson = new Gson();

        String userJson = gson.toJson(user);
        editor.putString("user",userJson);

        return editor.commit();
    }

    public static boolean removeUser(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.remove("user");

        return editor.commit();
    }
    public static User getUser(Context context){
        User user;
        SharedPreferences sharedpreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        String jsonUser = sharedpreferences.getString("user","");
        Gson gson = new Gson();
        user = (User) gson.fromJson(jsonUser,User.class);

        return user;
    }



    public static boolean saveToken(String token, Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("token",token);

        return editor.commit();
    }
    public static String getToken(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String token = sharedpreferences.getString("token","");
        return token;
    }

    public static void clearPreferences(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.clear();
        editor.commit();
    }

}