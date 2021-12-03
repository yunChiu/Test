package com.example.instagram.model;

public class DialogListItem {
    private int iconId = 0;
    private String item = "";

    public DialogListItem(int iconId, String item) {
        this.iconId = iconId;
        this.item = item;
    }

    public int getIconId() {
        return iconId;
    }

    public String getItem() {
        return item;
    }
}
