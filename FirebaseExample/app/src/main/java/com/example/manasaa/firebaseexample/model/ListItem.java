package com.example.manasaa.firebaseexample.model;

/**
 * Created by manasa.a on 24-03-2017.
 */

public class ListItem {
    private String name;
    private String url;
    public ListItem() {
    }

    public ListItem(String name, String url) {
        this.name = name;
        this.url= url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
