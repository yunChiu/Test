package com.example.instagram2;


import org.json.JSONException;
import org.json.JSONObject;

public class DataManager {
    public static void login(DataCallback dataCallback, String userName, String password) {
        String url = "http://211.76.157.47/igservices/CacheToken/Authenticate";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpManager okHttpManager = new OkHttpManager(dataCallback);
        okHttpManager.postData(url, jsonObject.toString());
    }

    public static void logout(DataCallback dataCallback, String accessToken) {
        String url = "http://211.76.157.47/igservices/CacheToken/RevokeTokenn";

        OkHttpManager okHttpManager = new OkHttpManager(dataCallback);
        okHttpManager.getData(url, accessToken);
    }

    public static void refreshToken(DataCallback dataCallback, String refreshToken) {
        String url = "http://211.76.157.47/igservices/CacheToken/RefreshToken";

        OkHttpManager okHttpManager = new OkHttpManager(dataCallback);
        okHttpManager.postData(url, refreshToken);
    }
}
