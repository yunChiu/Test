package com.example.instagram.model;

public class Followed {
    private String profile_pic_url_hd = "";
    private String userName = "";

    public Followed(String profile_pic_url_hd, String userName){
        this.profile_pic_url_hd = profile_pic_url_hd;
        this.userName = userName;
    }

    public String getProfile_pic_url_hd(){
        return profile_pic_url_hd;
    }

    public String getUserName(){
        return userName;
    }
}
