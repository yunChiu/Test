package com.example.instagram.model;

import java.io.Serializable;

public class HashTag implements Serializable {

    private String name = "";
    private String profile_pic_url = "";
    private Edge_hashtag_to_media edge_hashtag_to_media = new Edge_hashtag_to_media();

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }
    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public void setEdge_hashtag_to_media(Edge_hashtag_to_media edge_hashtag_to_media) {
        this.edge_hashtag_to_media = edge_hashtag_to_media;
    }
    public Edge_hashtag_to_media getEdge_hashtag_to_media() {
        return edge_hashtag_to_media;
    }

    public class Edge_hashtag_to_media {
        private int count = 0;
        private Edges edges = new Edges();

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
}
