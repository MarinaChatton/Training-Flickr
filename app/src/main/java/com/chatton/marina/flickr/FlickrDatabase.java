package com.chatton.marina.flickr;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Marina on 30/11/2016.
 */

@Database(name = FlickrDatabase.NAME, version = FlickrDatabase.VERSION)
public class FlickrDatabase {
    public static final String NAME = "FlickrDatabase";
    public static final int VERSION = 1;
}
