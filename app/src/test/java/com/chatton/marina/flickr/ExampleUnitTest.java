package com.chatton.marina.flickr;

import android.util.Log;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void shouldConvertFlickrResponse (){
        Converter converter = new Converter();

        //Raw Object
        PhotoDto photoDto1 = new PhotoDto("30270922721","145130459@N04","75c2ec3c42","8654",9,"Skeptic",1,0,0);
        PhotoDto photoDto2 = new PhotoDto("30240412862","145541034@N06","dafbdc93bf","7533",8,"DIY Dog Training",1,0,0);
        List<PhotoDto> photoDtoList = new ArrayList<>();
        photoDtoList.add(photoDto1);
        photoDtoList.add(photoDto2);
        FlickrPhotosDto photoSet = new FlickrPhotosDto(1,160887,2,"321774",photoDtoList);
        FlickrResponseDto response = new FlickrResponseDto(photoSet, "ok");

        //Converted Object
        List<Item> correctConversionResult = new ArrayList<>();
        Item photo1 = new Item("Skeptic", "https://farm9.static.flickr.com/8654/30270922721_75c2ec3c42.jpg");
        Item photo2 = new Item("DIY Dog Training", "https://farm8.static.flickr.com/7533/30240412862_dafbdc93bf.jpg");
        correctConversionResult.add(photo1);
        correctConversionResult.add(photo2);

        //conversion
        List<Item> convertedFlickResponse;
        convertedFlickResponse = converter.convert(response);

        //check
        assertEquals(correctConversionResult.toString(), convertedFlickResponse.toString());
    }
}