package com.chatton.marina.flickr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marina on 25/11/2016.
 */

public class Converter {
    public List<Photo> convert(FlickrResponseDto flickrResponseDto){
        List<Photo> convertedFlickResponse = new ArrayList<>();

        List<PhotoDto> photosDto = flickrResponseDto.getPhotos().getPhoto();
        for (PhotoDto photoDto : photosDto){
            String title = photoDto.getTitle();
            String url = "https://farm"+photoDto.getFarm()+".static.flickr.com/"+photoDto.getServer()+"/"+photoDto.getId()+"_"+photoDto.getSecret()+".jpg";
            convertedFlickResponse.add(new Photo(title, url));
        }

        return convertedFlickResponse;
    }
}
