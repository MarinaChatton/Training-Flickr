package com.chatton.marina.flickr;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

/**
 * Created by Marina on 24/11/2016.
 */
@Table(database = FlickrDatabase.class)
public class Photo extends BaseModel implements Serializable {
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    private String title;
    @Column
    @Unique
    private String url;

    public Photo() {
    }

    public Photo(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
