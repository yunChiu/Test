package com.example.instagram.model;

import java.util.ArrayList;

public class Node {
    private String shortcode = ""; //分享貼文用
    private String display_url = ""; //頭貼
    private boolean is_video = false;
    private Edge_media_to_caption edge_media_to_caption = new Edge_media_to_caption(); //貼文內容
    private Edge_media_to_comment edge_media_to_comment = new Edge_media_to_comment(); //留言數
    private int taken_at_timestamp = 0; //發文時間
    private Edge_liked_by edge_liked_by = new Edge_liked_by(); //愛心數
    private Edge_media_preview_like edge_media_preview_like = new Edge_media_preview_like(); //影片觀看人數
    private Location location = new Location(); //地點
    private Edge_sidecar_to_children edge_sidecar_to_children = new Edge_sidecar_to_children();

    //class
    public class Edge_media_to_caption {
        private Edges_inner edges = new Edges_inner();
        public class Edges_inner {
            private Node_inner node = new Node_inner();
            public class Node_inner {
                private String text = "";

                public void setText(String text) {
                    this.text = text;
                }
                public String getText() {
                    return text;
                }
            }

            public void setNode_inner(Node_inner node) {
                this.node = node;
            }
            public Node_inner getNode_inner() {
                return node;
            }
        }

        public void setEdges_inner(Edges_inner edges) {
            this.edges = edges;
        }
        public Edges_inner getEdges_inner() {
            return edges;
        }
    }

    public class Edge_media_to_comment {
        private int count = 0;

        public void setCount(int count) {
            this.count = count;
        }
        public int getCount() {
            return count;
        }
    }

    public class Edge_liked_by {
        private int count = 0;

        public void setCount(int count) {
            this.count = count;
        }
        public int getCount() {
            return count;
        }
    }

    public class Edge_media_preview_like {
        private int count = 0;

        public void setCount(int count) {
            this.count = count;
        }
        public int getCount() {
            return count;
        }
    }

    public class Location {
        private String name = "";

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }

    public class Edge_sidecar_to_children {
        ArrayList<Node> nodeList = new ArrayList<>();

        public void setNodeList(ArrayList<Node> nodeList) {
            this.nodeList = nodeList;
        }
        public ArrayList<Node> getNodeList() {
            return nodeList;
        }
    }

    //getter setter
    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }
    public String getShortcode() {
        return shortcode;
    }

    public void setDisplay_url(String display_url) {
        this.display_url = display_url;
    }
    public String getDisplay_url() {
        return display_url;
    }

    public void setIs_video(boolean is_video) {
        this.is_video = is_video;
    }
    public boolean getIs_video() {
        return is_video;
    }

    public void setEdge_media_to_caption(Edge_media_to_caption edge_media_to_caption) {
        this.edge_media_to_caption = edge_media_to_caption;
    }
    public Edge_media_to_caption getEdge_media_to_caption() {
        return edge_media_to_caption;
    }

    public void setEdge_media_to_comment(Edge_media_to_comment edge_media_to_comment) {
        this.edge_media_to_comment = edge_media_to_comment;
    }
    public Edge_media_to_comment getEdge_media_to_comment() {
        return edge_media_to_comment;
    }

    public  void setTaken_at_timestamp(int taken_at_timestamp) {
        this.taken_at_timestamp = taken_at_timestamp;
    }
    public int getTaken_at_timestamp() {
        return taken_at_timestamp;
    }

    public void setEdge_liked_by(Edge_liked_by edge_liked_by) {
        this.edge_liked_by = edge_liked_by;
    }
    public Edge_liked_by getEdge_liked_by() {
        return edge_liked_by;
    }

    public void setEdge_media_preview_like(Edge_media_preview_like edge_media_preview_like) {
        this.edge_media_preview_like = edge_media_preview_like;
    }
    public Edge_media_preview_like getEdge_media_preview_like() {
        return edge_media_preview_like;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    public Location getLocation() {
        return location;
    }

    public void setEdge_sidecar_to_children(Edge_sidecar_to_children edge_sidecar_to_children) {
        this.edge_sidecar_to_children = edge_sidecar_to_children;
    }
    public Edge_sidecar_to_children getEdge_sidecar_to_children() {
        return edge_sidecar_to_children;
    }
}
