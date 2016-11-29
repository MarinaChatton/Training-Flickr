package com.chatton.marina.flickr;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, FlickrResponseListener, AdapterView.OnItemSelectedListener, ToggleButton.OnValueChangedListener {
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private FlickrService flickrService;
    boolean bound = false;

    private List<Photo> photoList = new ArrayList<>();
    private ListAdapter adapter = new ListAdapter(this);

    //default values:
    private int displayModeIndex = 0;
    private int imagePerPageIndex = 0;

    private String imagePerPage;

    private SharedPreferences sharedPreferences;
    private final static String DISPLAY_MODE_INDEX = "displayModeIndex";
    private final static String IMAGE_PER_PAGE_INDEX = "imagePerPageIndex";

    //drawer
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.drawer_toggle_button) MultiStateToggleButton drawerToggle;
    @BindView(R.id.drawer_images_number_per_page) Spinner drawerImagesNbSpinner;

    //search view
    @BindView(R.id.search_layout) LinearLayout searchLayout;
    @BindView(R.id.search_text) EditText searchText;
    @BindView(R.id.search_button) ImageButton searchButton;
    @BindView(R.id.list) ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getPreferences(MODE_PRIVATE);

        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //actionBar
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //drawer:
        //toggle button to select display mode
        initDrawerMultiStateButtons();
        //dropdown to set the number of images displayed
        if(sharedPreferences.contains(IMAGE_PER_PAGE_INDEX)){
            imagePerPageIndex = sharedPreferences.getInt(IMAGE_PER_PAGE_INDEX, 0);
        }
        drawerImagesNbSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,R.array.images_number_per_page, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drawerImagesNbSpinner.setAdapter(adapterSpinner);
        drawerImagesNbSpinner.setSelection(imagePerPageIndex);
        imagePerPage = (String) drawerImagesNbSpinner.getItemAtPosition(imagePerPageIndex);

        //list setting
        listView.setAdapter(adapter);
        adapter.setPhotoList(photoList);
        listView.setOnItemClickListener(this);
        //search button
        searchButton.setOnClickListener(this);
        setSearchLayoutVisibility(sharedPreferences.getInt(DISPLAY_MODE_INDEX,0));
    }

    public void initDrawerMultiStateButtons(){
        if(sharedPreferences.contains(DISPLAY_MODE_INDEX)){
            displayModeIndex = sharedPreferences.getInt(DISPLAY_MODE_INDEX, 0);
        }
        final ImageButton button1 = (ImageButton) getLayoutInflater().inflate(R.layout.drawer_search_button, drawerToggle, false);
        button1.setImageResource(android.R.drawable.ic_menu_search);
        ImageButton button2 = (ImageButton) getLayoutInflater().inflate(R.layout.drawer_search_button, drawerToggle, false);
        button2.setImageResource(android.R.drawable.ic_menu_recent_history);

        View[] buttons = new View[] {button1, button2};
        boolean[] selectionState = new boolean[buttons.length];
        selectionState[displayModeIndex] = true;
        drawerToggle.setButtons(buttons, selectionState);
        drawerToggle.setOnValueChangedListener(this);
    }

    public void setSearchLayoutVisibility(int value){
        switch (value){
            case 0:
                searchLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                searchLayout.setVisibility(View.GONE);
                break;
            default:
                break;
        }
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

    //drawer togglebutton listener
    @Override
    public void onValueChanged(int value) {
        setSearchLayoutVisibility(value);
        //save preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(DISPLAY_MODE_INDEX, value);
        editor.commit();
    }

    //drawer dropdown listener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        imagePerPage = (String) parent.getItemAtPosition(position);
        if(bound) {
            flickrService.getPhotoList(imagePerPage, searchText.getText().toString());
        }
        //save preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(IMAGE_PER_PAGE_INDEX, position);
        editor.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
