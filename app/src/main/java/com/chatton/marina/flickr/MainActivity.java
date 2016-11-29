package com.chatton.marina.flickr;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.IBinder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, FlickrResponseListener, AdapterView.OnItemSelectedListener {
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private FlickrService flickrService;
    boolean bound = false;

    private String imagePerPage;
    private List<Photo> photoList = new ArrayList<>();
    private ListAdapter adapter = new ListAdapter(this);

    //drawer
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.drawer_search_button) ImageButton drawerSearchButton;
    @BindView(R.id.drawer_history_button) Button drawerHistoryButton;
    @BindView(R.id.drawer_images_number_per_page) Spinner drawerImagesNumberPerPage;

    //search view
    @BindView(R.id.list) ListView listView;
    @BindView(R.id.search_text) EditText searchText;
    @BindView(R.id.search_button) ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //actionBar
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close){/** Called when a drawer has settled in a completely closed state. */
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            getSupportActionBar().setTitle("Drawer");
        }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Drawer");
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        //drawer
        int defaultSelectedIndex = 0;
        Spinner spinner = (Spinner) findViewById(R.id.drawer_images_number_per_page);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,R.array.images_number_per_page, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);
        spinner.setSelection(defaultSelectedIndex);
        imagePerPage = (String) spinner.getItemAtPosition(defaultSelectedIndex);

        //list setting
        listView.setAdapter(adapter);
        adapter.setPhotoList(photoList);
        listView.setOnItemClickListener(this);
        //search button
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
            flickrService.getPhotoList(imagePerPage, searchText.getText().toString());
        }
    }

    @Override
    public void onPhotoReceived(List<Photo> photoList) {
        adapter.setPhotoList(photoList);
    }

    //actionbar toggle button
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //drawer dropdown listener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        imagePerPage = (String) parent.getItemAtPosition(position);
        if(bound) {
            flickrService.getPhotoList(imagePerPage, searchText.getText().toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
