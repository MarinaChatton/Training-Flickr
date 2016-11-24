package com.chatton.marina.flickr;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marina on 22/11/2016.
 */

public class ListAdapter extends BaseAdapter{
    private List<Item> itemsList = new ArrayList<>();
    private Context context;

    public ListAdapter() {
    }

    public ListAdapter(Context context) {
        this.context = context;
    }

    public List<Item> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<Item> itemsList) {
        this.itemsList = itemsList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public Item getItem(int position) {
        return itemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false);

        }
        final Item item = getItem(position);
        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
        Picasso.with(context).load(item.getUrl()).resize(200, 200).centerInside().into(thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.item_title);
        title.setText(item.getTitle());
        FloatingActionButton fab = (FloatingActionButton) convertView.findViewById(R.id.fab_delete);
        fab.setFocusable(false);
        fab.setFocusableInTouchMode(false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsList.remove(item);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
