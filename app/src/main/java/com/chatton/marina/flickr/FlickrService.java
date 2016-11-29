package com.chatton.marina.flickr;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

public class FlickrService extends Service{
    public static final String URL = " https://www.flickr.com";
    private final IBinder binder = new ServiceBinder();
    private List<Photo> photoList = new ArrayList<>();
    private Converter converter = new Converter();
    private Retrofit retrofit;
    private FlickrRetrofitService service;
    private FlickrResponseListener flickrResponseListener;

    public void setFlickrResponseListener(FlickrResponseListener flickrResponseListener) {
        this.flickrResponseListener = flickrResponseListener;
    }

    public void getPhotoList(String perPage, String query) {
        if(!query.equals("")) {
            query = formatQuery(query);
            Call<FlickrResponseDto> flickrResponseDtoCall = service.getPhotos(perPage, query, getResources().getString(R.string.flickr_api_key));
            flickrResponseDtoCall.enqueue(new Callback<FlickrResponseDto>() {
                @Override
                public void onResponse(Call<FlickrResponseDto> call, Response<FlickrResponseDto> response) {
                    if (response.isSuccessful()) {
                        photoList = converter.convert(response.body());
                        flickrResponseListener.onPhotoReceived(photoList);
                    } else {
                        Log.e("unSuccessful", "url: " + call.request().url());
                    }
                }

                @Override
                public void onFailure(Call<FlickrResponseDto> call, Throwable t) {
                    Log.e("onFailure", String.valueOf(t));
                }
            });
        }
    }

    public String formatQuery(String query){
        query = query.trim();
        query = query.replaceAll("\\s+","+");
        return query;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        String apiKey = getResources().getString(R.string.flickr_api_key);
        retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(FlickrRetrofitService.class);
        return binder;
    }

    public class ServiceBinder extends Binder {
        FlickrService getService() {
            return FlickrService.this;
        }
    }
}
