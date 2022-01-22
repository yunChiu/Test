package com.example.instagram.model;

import java.io.Serializable;

public class Tag implements Serializable {
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
