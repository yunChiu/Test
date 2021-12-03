package com.example.instagram.model;

public class User {

    private String biography = ""; //自我介紹
    private String external_url = ""; //個人連結
    private Count edge_followed_by = new Count(); //粉絲數
    private Count edge_follow = new Count(); //追蹤數
    private String full_name = ""; //名字
    private String profile_pic_url_hd = ""; //頭像
    private String username = ""; //帳號名稱

    private Timeline edge_felix_video_timeline = new Timeline(); //post_video
    private Timeline edge_owner_to_timeline_media = new Timeline(); //post_grid

    public class Count {
        public int count = 0;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    public class Timeline {
        public int count = 0;
        public Edges edges = new Edges();

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public Edges getEdges() {
            return edges;
        }

        public void setEdges(Edges edges) {
            this.edges = edges;
        }
    }

    public String getBiography() {
        return biography;
    }
    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getExternal_url() {
        return external_url;
    }
    public void setExternal_url(String external_url) {
        this.external_url = external_url;
    }

    public Count getEdge_followed_by() {
        return edge_followed_by;
    }
    public void setEdge_followed_by(Count edge_followed_by) {
        this.edge_followed_by = edge_followed_by;
    }

    public Count getEdge_follow() {
        return edge_follow;
    }
    public void setEdge_follow(Count edge_follow) {
        this.edge_follow = edge_follow;
    }

    public String getFull_name() {
        return full_name;
    }
    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getProfile_pic_url_hd() {
        return profile_pic_url_hd;
    }
    public void setProfile_pic_url_hd(String profile_pic_url_hd) {
        this.profile_pic_url_hd = profile_pic_url_hd;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public Timeline getEdge_felix_video_timeline() {
        return edge_felix_video_timeline;
    }
    public void setEdge_felix_video_timeline(Timeline edge_felix_video_timeline) {
        this.edge_felix_video_timeline = edge_felix_video_timeline;
    }

    public Timeline getEdge_owner_to_timeline_media() {
        return edge_owner_to_timeline_media;
    }
    public void setEdge_owner_to_timeline_media(Timeline edge_owner_to_timeline_media) {
        this.edge_owner_to_timeline_media = edge_owner_to_timeline_media;
    }
}
