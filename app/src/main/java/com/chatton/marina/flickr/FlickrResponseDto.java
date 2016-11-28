package com.chatton.marina.flickr;

/**
 * Created by Marina on 25/11/2016.
 */

public class FlickrResponseDto {
    private FlickrPhotosDto photos;
    private String stat;

    //required by Java Bean convention to perform a web call
    public FlickrResponseDto() {
    }

    public FlickrResponseDto(FlickrPhotosDto photos, String stat) {
        this.photos = photos;
        this.stat = stat;
    }

    public FlickrPhotosDto getPhotos() {
        return photos;
    }

    public void setPhotos(FlickrPhotosDto photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    @Override
    public String toString() {
        return "FlickrResponseDto{" +
                "photos=" + photos.toString() +
                ", stat='" + stat + '\'' +
                '}';
    }
}
