package com.example.instagram2.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class MyNode implements Serializable {
    private String shortCode = "";
    private String display_url = "";
    private String text = "";
    private int taken_at_timestamp = 0;
    private int edge_media_to_comment_count = 0;
    private int edge_liked_by_count = 0;
    //private String location_name = "";
    private ArrayList<String> photoUrlList = null;

    public MyNode(String display_url, String text, int taken_at_timestamp, int edge_media_to_comment_count, int edge_liked_by_count, ArrayList<String> photoUrlList){
        this.display_url = display_url;
        this.text = text;
        this.taken_at_timestamp = taken_at_timestamp;
        this.edge_media_to_comment_count = edge_media_to_comment_count;
        this.edge_liked_by_count = edge_liked_by_count;
        //this.location_name = location_name;
        this.photoUrlList = photoUrlList;
    }

    public String getDisplay_url(){
        return display_url;
    }
    public String getText(){
        return text;
    }
    public int getTaken_at_timestamp(){
        return taken_at_timestamp;
    }
    public int getEdge_media_to_comment_count(){
        return edge_media_to_comment_count;
    }
    public int getEdge_liked_by_count(){
        return edge_liked_by_count;
    }
    /*
    public String getLocation_name(){
        return location_name;
    }
    */
    public ArrayList<String> getPhotoUrlList(){
        return photoUrlList;
    }
}