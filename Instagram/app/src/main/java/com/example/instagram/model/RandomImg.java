package com.example.instagram.model;

import java.util.ArrayList;

public class RandomImg {
    private String id = "";
    private String author = "";
    private int width = 0;
    private int height = 0;
    private String url = "";
    private String download_url = "";

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public String getAuthor() {
        return author;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    public int getHeight() {
        return height;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }
    public String getDownload_url() {
        return download_url;
    }
}
