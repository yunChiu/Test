package com.example.instagram.model;


public class Node {
    private String shortcode = "";
    private Dimensions dimensions = new Dimensions();
    private String display_url = ""; //頭貼
    private boolean is_video = false;
    private Edge_media_to_caption edge_media_to_caption = new Edge_media_to_caption(); //內容
    private Count edge_media_to_comment = new Count(); //留言數
    private int taken_at_timestamp = 0; //發文時間
    private Count edge_liked_by = new Count(); //愛心數
    private Count edge_media_preview_like = new Count(); //影片觀看人數
    private Location location = new Location(); //地點
    private Edges edge_sidecar_to_children = new Edges();
    private double video_duration = 0.0;

    //class
    public class Dimensions {
        public int height = 0;
        public int width = 0;

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }

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

    public class Count {
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

    //getter setter
    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }
    public String getShortcode() {
        return shortcode;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }
    public Dimensions getDimensions() {
        return dimensions;
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

    public void setEdge_media_to_comment(Count edge_media_to_comment) {
        this.edge_media_to_comment = edge_media_to_comment;
    }
    public Count getEdge_media_to_comment() {
        return edge_media_to_comment;
    }

    public  void setTaken_at_timestamp(int taken_at_timestamp) {
        this.taken_at_timestamp = taken_at_timestamp;
    }
    public int getTaken_at_timestamp() {
        return taken_at_timestamp;
    }

    public void setEdge_liked_by(Count edge_liked_by) {
        this.edge_liked_by = edge_liked_by;
    }
    public Count getEdge_liked_by() {
        return edge_liked_by;
    }

    public void setEdge_media_preview_like(Count edge_media_preview_like) {
        this.edge_media_preview_like = edge_media_preview_like;
    }
    public Count getEdge_media_preview_like() {
        return edge_media_preview_like;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    public Location getLocation() {
        return location;
    }

    public void setEdge_sidecar_to_children(Edges edge_sidecar_to_children) {
        this.edge_sidecar_to_children = edge_sidecar_to_children;
    }
    public Edges getEdge_sidecar_to_children() {
        return edge_sidecar_to_children;
    }

    public void setVideo_duration(double video_duration) {
        this.video_duration = video_duration;
    }
    public double getVideo_duration() {
        return video_duration;
    }
}
