package com.chatton.marina.flickr;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
        Picasso.with(context).load(photo.getUrl()).resize(200, 200).centerInside().into(thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.item_title);
        title.setText(photo.getTitle());
        FloatingActionButton fab = (FloatingActionButton) convertView.findViewById(R.id.fab_delete);
        fab.setFocusable(false);
        fab.setFocusableInTouchMode(false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = ((Activity) context).getPreferences(Context.MODE_PRIVATE);
                int displayModeIndex = sp.getInt(MainActivity.DISPLAY_MODE_INDEX, 0);
                if(displayModeIndex==1){
                    PhotoPersistenceManager photoPersistenceManager = new PhotoPersistenceManager(context);
                    photoPersistenceManager.delete(photo);
                }
                photoList.remove(photo);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
