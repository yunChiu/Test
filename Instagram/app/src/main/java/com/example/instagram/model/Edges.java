package com.example.instagram.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Edges implements Serializable {
    private ArrayList<Node> nodeList = new ArrayList<>();

    public ArrayList<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(ArrayList<Node> nodeList) {
        this.nodeList = nodeList;
    }
}
