package com.chatton.marina.flickr;

import java.io.Serializable;

/**
 * Created by Marina on 24/11/2016.
 */

public class Item implements Serializable{
    private String title;
    private String url;

    public Item() {
    }

    public Item(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
