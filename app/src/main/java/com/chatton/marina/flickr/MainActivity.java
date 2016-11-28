package com.chatton.marina.flickr;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, FlickrResponseListener {
    private FlickrService flickrService;
    boolean bound = false;

    private List<Photo> photoList = new ArrayList<>();
    private ListAdapter adapter = new ListAdapter(this);

    @BindView(R.id.list) ListView listView;
    @BindView(R.id.search_text) EditText searchText;
    @BindView(R.id.search_button) ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        listView.setAdapter(adapter);

        adapter.setPhotoList(photoList);

        listView.setOnItemClickListener(this);

        searchButton.setOnClickListener(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = new Intent(this, FlickrService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(bound){
            unbindService(connection);
            bound = false;
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            FlickrService.ServiceBinder binder = (FlickrService.ServiceBinder) service;
            flickrService = binder.getService();
            //declare MainActivity as listener of FlickrResponseListener
            flickrService.setFlickrResponseListener(MainActivity.this);
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Photo photo = adapter.getItem(position);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("photo", photo);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(bound) {
            flickrService.getPhotoList(searchText.getText().toString());
        }
    }

    @Override
    public void onPhotoReceived(List<Photo> photoList) {
        adapter.setPhotoList(photoList);
    }
}
