package com.chatton.marina.flickr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marina on 28/11/2016.
 */

public interface FlickrResponseListener {
    public void onPhotoReceived(List<Photo> photoList);
}
