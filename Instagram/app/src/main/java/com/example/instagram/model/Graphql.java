package com.example.instagram.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Graphql implements Serializable {
    private User user = new User();

    public class User {
        private String biography = ""; //自我介紹
        private String external_url = ""; //個人連結
        private Edge_followed_by edge_followed_by = new Edge_followed_by(); //粉絲數
        private Edge_follow edge_follow = new Edge_follow(); //追蹤數
        private String full_name = ""; //全名
        private String profile_pic_url_hd = ""; //頭像
        private String username = ""; //帳號
        private Edge_owner_to_timeline_media edge_owner_to_timeline_media = new Edge_owner_to_timeline_media(); //貼文資料
        //class
        public class Edge_followed_by {
            private int count = 0;

            public void setCount(int count) {
                this.count = count;
            }
            public int getCount() {
                return count;
            }
        }
        public class Edge_follow {
            private int count = 0;

            public void setCount(int count) {
                this.count = count;
            }
            public int getCount() {
                return count;
            }
        }
        public class Edge_owner_to_timeline_media {
            private int count = 0; //貼文數
            private Edges edges = new Edges();

            public class Edges {
                private ArrayList<Node> nodeList = new ArrayList<>();

                public void setNodeList(ArrayList<Node> nodeList) {
                    this.nodeList = nodeList;
                }
                public ArrayList<Node> getNodeList() {
                    return nodeList;
                }
            }

            public void setCount(int count) {
                this.count = count;
            }
            public int getCount() {
                return count;
            }

            public void setEdges(Edges edges) {
                this.edges = edges;
            }
            public Edges getEdges() {
                return edges;
            }
        }
        //getter setter
        public void setBiography(String biography) {
            this.biography = biography;
        }
        public String getBiography() {
            return biography;
        }

        public void setExternal_url(String external_url) {
            this.external_url = external_url;
        }
        public String getExternal_url() {
            return external_url;
        }

        public void setEdge_followed_by(Edge_followed_by edge_followed_by) {
            this.edge_followed_by = edge_followed_by;
        }
        public Edge_followed_by getEdge_followed_by() {
            return edge_followed_by;
        }

        public void setEdge_follow(Edge_follow edge_follow) {
            this.edge_follow = edge_follow;
        }
        public Edge_follow getEdge_follow() {
            return edge_follow;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }
        public String getFull_name() {
            return full_name;
        }

        public void setProfile_pic_url_hd(String profile_pic_url_hd) {
            this.profile_pic_url_hd = profile_pic_url_hd;
        }
        public String getProfile_pic_url_hd() {
            return profile_pic_url_hd;
        }

        public void setUsername(String username) {
            this.username = username;
        }
        public String getUsername() {
            return username;
        }

        public void setEdge_owner_to_timeline_media(Edge_owner_to_timeline_media edge_owner_to_timeline_media) {
            this.edge_owner_to_timeline_media = edge_owner_to_timeline_media;
        }
        public Edge_owner_to_timeline_media getEdge_owner_to_timeline_media() {
            return edge_owner_to_timeline_media;
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
