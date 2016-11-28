package com.chatton.marina.flickr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marina on 25/11/2016.
 */

public class FlickrPhotosDto {
    private long page;
    private long pages;
    private long perpage;
    private String total;
    private List<PhotoDto> photo = new ArrayList<>();

    //required by Java Bean convention
    public FlickrPhotosDto() {
    }

    public FlickrPhotosDto(long page, long pages, long perpage, String total, List<PhotoDto> photo) {
        this.page = page;
        this.pages = pages;
        this.perpage = perpage;
        this.total = total;
        this.photo = photo;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }

    public long getPerpage() {
        return perpage;
    }

    public void setPerpage(long perpage) {
        this.perpage = perpage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<PhotoDto> getPhoto() {
        return photo;
    }

    public void setPhoto(List<PhotoDto> photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "FlickrPhotosDto{" +
                "page=" + page +
                ", pages=" + pages +
                ", perpage=" + perpage +
                ", total='" + total + '\'' +
                ", photo=" + photo.toString() +
                '}';
    }
}
