package com.example.instagram2.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String biography = ""; //自我介紹
    private String external_url = ""; //個人連結(沒用到)
    private int followed_by = 0; //追蹤數
    private String full_name = ""; //名字
    private String profile_pic_url_hd = ""; //頭像
    private String username = ""; //帳號名稱
    private int total_post = 0; //貼文數量
    private ArrayList<MyNode> myNode = new ArrayList<>();
    //private Edge_sidecar_to_children edge_sidecar_to_children;

    public void setBiography(String biography){
        this.biography = biography;
    }
    public String getBiography(){
        return biography;
    }

    public void setExternal_url(String external_url){
        this.external_url = external_url;
    }
    public String getExternal_url(){
        return external_url;
    }

    public void setFollowed_by(int followed_by){
        this.followed_by = followed_by;
    }
    public int getFollowed_by(){
        return followed_by;
    }

    public void setFull_name(String full_name){
        this.full_name = full_name;
    }
    public String getFull_name(){
        return full_name;
    }

    public void setProfile_pic_url_hd(String profile_pic_url_hd){
        this.profile_pic_url_hd = profile_pic_url_hd;
    }
    public String getProfile_pic_url_hd(){
        return profile_pic_url_hd;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return username;
    }

    public void setTotal_post(int total_post){
        this.total_post = total_post;
    }
    public int getTotal_post(){
        return total_post;
    }

    public void setMyNode(ArrayList<MyNode> myNode){
        this.myNode = myNode;
    }
    public ArrayList<MyNode> getMyNode(){
        return myNode;
    }
}
