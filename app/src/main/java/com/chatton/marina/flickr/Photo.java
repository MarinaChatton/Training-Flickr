package com.chatton.marina.flickr;

import java.io.Serializable;

/**
 * Created by Marina on 24/11/2016.
 */

public class Photo implements Serializable{
    private String title;
    private String url;

    public Photo() {
    }

    public Photo(String title, String url) {
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
        return "Photo{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}