package com.example.instagram2.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Tag implements Serializable {
    private Graphql graphql = new Graphql();

    public class Graphql {
        private Hashtag hashtag = new Hashtag();

        public class Hashtag {
            private String name = "";
            private Edge_hashtag_to_media edge_hashtag_to_media = new Edge_hashtag_to_media(); //最近貼文
            private Edge_hashtag_to_top_posts edge_hashtag_to_top_posts = new Edge_hashtag_to_top_posts(); //熱門貼文

            public class Edge_hashtag_to_media {
                private int count = 0;
                private Edges edges = new Edges();

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

            public void setEdge_hashtag_to_media(Edge_hashtag_to_media edge_hashtag_to_media) {
                this.edge_hashtag_to_media = edge_hashtag_to_media;
            }
            public Edge_hashtag_to_media getEdge_hashtag_to_media() {
                return edge_hashtag_to_media;
            }

            public class Edge_hashtag_to_top_posts {
                private Edges edges = new Edges();

                public void setEdges(Edges edges) {
                    this.edges = edges;
                }
                public Edges getEdges() {
                    return edges;
                }
            }

            public void setName(String name) {
                this.name = name;
            }
            public String getName() {
                return name;
            }

            public void setEdge_hashtag_to_top_posts(Edge_hashtag_to_top_posts edge_hashtag_to_top_posts) {
                this.edge_hashtag_to_top_posts = edge_hashtag_to_top_posts;
            }
            public Edge_hashtag_to_top_posts getEdge_hashtag_to_top_posts() {
                return edge_hashtag_to_top_posts;
            }
        }

        public void setHashtag(Hashtag hashtag) {
            this.hashtag = hashtag;
        }
        public Hashtag getHashtag() {
            return hashtag;
        }
    }

    public void setGraphql(Graphql graphql) {
        this.graphql = graphql;
    }
    public Graphql getGraphql() {
        return graphql;
    }

    public class Edges {
        private ArrayList<Node> nodeList = new ArrayList<>();

        public void setNodeList(ArrayList<Node> nodeList) {
            this.nodeList = nodeList;
        }
        public ArrayList<Node> getNodeList() {
            return nodeList;
        }
    }
}