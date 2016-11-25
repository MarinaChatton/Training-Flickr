package com.chatton.marina.flickr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Photo photo = (Photo) getIntent().getSerializableExtra("photo");

        TextView title = (TextView) findViewById(R.id.detail_title);
        title.setText(photo.getTitle().toUpperCase());

        TextView url = (TextView) findViewById(R.id.detail_url);
        url.setText(photo.getUrl());

        ImageView img = (ImageView) findViewById(R.id.detail_img);
        Picasso.with(this).load(photo.getUrl()).into(img);
    }
}
