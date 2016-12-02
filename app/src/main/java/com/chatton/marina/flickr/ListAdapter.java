package com.chatton.marina.flickr;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marina on 22/11/2016.
 */

public class ListAdapter extends BaseAdapter{
    private List<Photo> photoList = new ArrayList<>();
    private Context context;
    private OnRowDeletedListener onRowDeletedListener;

    public ListAdapter() {
    }

    public ListAdapter(Context context) {
        this.context = context;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
        notifyDataSetChanged();
    }

    public void setOnRowDeletedListener(OnRowDeletedListener onRowDeletedListener) {
        this.onRowDeletedListener = onRowDeletedListener;
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public Photo getItem(int position) {
        return photoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false);

        }
        final Photo photo = getItem(position);
        final ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
        Picasso.with(context).load(photo.getUrl()).resize(200, 200).centerInside().into(thumbnail);
        final TextView title = (TextView) convertView.findViewById(R.id.item_title);
        title.setText(photo.getTitle());
        setStarCounter(convertView ,photo);
        FloatingActionButton fab = (FloatingActionButton) convertView.findViewById(R.id.fab_delete);
        fab.setFocusable(false);
        fab.setFocusableInTouchMode(false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onRowDeletedListener!=null) {
                    onRowDeletedListener.onRowDeleted(photo);
                }
                photoList.remove(photo);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    private void setStarCounter(View convertView ,Photo photo){
        final SharedPreferences sp = ((Activity) context).getPreferences(Context.MODE_PRIVATE);
        final String displayMode = sp.getString(MainActivity.DISPLAY_MODE, MainActivity.SEARCH_MODE);
        ImageButton star = (ImageButton) convertView.findViewById(R.id.item_star);
        star.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        star.setFocusable(false);
        star.setFocusableInTouchMode(false);
        TextView clickCounter = (TextView) convertView.findViewById(R.id.item_click_counter);
        if(displayMode.equals(MainActivity.HISTORY_MODE)){
            clickCounter.setText(String.valueOf(photo.getClickCounter()));
        }else{
            clickCounter.setText("");
            PhotoPersistenceManager photoPersistenceManager = new PhotoPersistenceManager(context);
            Photo savedPhoto = photoPersistenceManager.getByUrl(photo.getUrl());
            if(savedPhoto==null) {
                star.setColorFilter(ContextCompat.getColor(context, android.R.color.white), PorterDuff.Mode.MULTIPLY);
            }
        }
    }
}
