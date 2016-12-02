package com.chatton.marina.flickr;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, FlickrResponseListener, AdapterView.OnItemSelectedListener, ToggleButton.OnValueChangedListener, OnRowDeletedListener {
    //storage keys
    public static final String FULL_VIEW_PHOTO = "photo";
    public final static String DISPLAY_MODE = "displayMode";
    public final static String IMAGE_PER_PAGE_INDEX = "imagePerPageIndex";

    public final static String SEARCH_MODE = "search";
    public final static String HISTORY_MODE = "history";

    //persistence
    private SharedPreferences sharedPreferences;
    private PhotoPersistenceManager photoPersistenceManager;

    //service
    private FlickrService flickrService;
    boolean bound = false;

    //list
    private List<Photo> photoList = new ArrayList<>();
    private ListAdapter adapter = new ListAdapter(this);

    //GRAPHIC COMPONENTS
    //action bar button
    private ActionBarDrawerToggle actionBarDrawerToggle;

    //drawer
    DrawerLayout drawerLayout;
    MultiStateToggleButton drawerToggle;
    Spinner drawerImagesNbSpinner;
    //TODO set a public static List of HashMaps {"displayMode": displayMode, "icon": drawable}, and generate buttons in MultiStateToggleButtons with for loop + add method to get IndexByMode

    //search view
    LinearLayout searchLayout;
    EditText searchText;
    ImageButton searchButton;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        adapter.setOnRowDeletedListener(MainActivity.this);

        sharedPreferences = getPreferences(MODE_PRIVATE);
        photoPersistenceManager = new PhotoPersistenceManager(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //VIEWS
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = (MultiStateToggleButton) findViewById(R.id.drawer_toggle_button);
        drawerImagesNbSpinner = (Spinner) findViewById(R.id.drawer_images_number_per_page);
        searchLayout = (LinearLayout) findViewById(R.id.search_layout);
        searchText = (EditText) findViewById(R.id.search_text);
        searchButton = (ImageButton) findViewById(R.id.search_button);
        listView = (ListView) findViewById(R.id.list);

        //ACTION BAR
        initActionBar();

        //DRAWER
        //toggle button to select display mode:
        initDrawerMultiStateButtons();
        //dropdown to set the number of images displayed:
        initSpinner();

        //SEARCH VIEW
        //search bar
        initSearchBar();
        //list setting
        initList();

    }

    private void initActionBar() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setNavBarTitle(getDisplayMode());
    }

    private void initDrawerMultiStateButtons(){
        ImageButton button1 = (ImageButton) getLayoutInflater().inflate(R.layout.drawer_search_button, drawerToggle, false);
        button1.setImageResource(android.R.drawable.ic_menu_search);
        ImageButton button2 = (ImageButton) getLayoutInflater().inflate(R.layout.drawer_search_button, drawerToggle, false);
        button2.setImageResource(android.R.drawable.ic_menu_recent_history);

        View[] buttons = new View[] {button1, button2};
        boolean[] selectionState = new boolean[buttons.length];
        selectionState[getDisplayModeButtonIndexByDisplayMode(getDisplayMode())] = true;
        drawerToggle.setButtons(buttons, selectionState);
        drawerToggle.setOnValueChangedListener(this);
    }

    private void initSpinner(){
        drawerImagesNbSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,R.array.images_number_per_page, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drawerImagesNbSpinner.setAdapter(adapterSpinner);
        drawerImagesNbSpinner.setSelection(sharedPreferences.getInt(IMAGE_PER_PAGE_INDEX, 0));//if saved spinner's value as string instead of index of value => spinner.setSelection(adapter.getPosition(value))
    }

    private void initSearchBar(){
        searchButton.setOnClickListener(this);
        setSearchLayoutVisibility(getDisplayMode());
    }

    private void initList(){
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    //TODO replace with display mode string constant
    public String getDisplayMode(){
        return sharedPreferences.getString(DISPLAY_MODE, SEARCH_MODE);
    }

    //TODO use it in onValueChanged to save the display mode
    private String getDisplayModeByButtonIndex(int buttonIndex){
        String displayMode = SEARCH_MODE;
        switch (buttonIndex){
            case 0:
                displayMode = SEARCH_MODE;
                break;
            case 1:
                displayMode = HISTORY_MODE;
                break;
        }
        return displayMode;
    }

    private int getDisplayModeButtonIndexByDisplayMode(String displayMode){
        int buttonIndex = 0;
        switch (displayMode){
            case SEARCH_MODE:
                buttonIndex = 0;
                break;
            case HISTORY_MODE:
                buttonIndex = 1;
                break;
        }
        return buttonIndex;
    }

    private void setSearchLayoutVisibility(String displayMode){
        switch (displayMode){
            case SEARCH_MODE:
                searchLayout.setVisibility(View.VISIBLE);
                break;
            case HISTORY_MODE:
                searchLayout.setVisibility(View.GONE);
                break;
            default:
                searchLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setNavBarTitle(String displayMode){
        String navBarTitle = getResources().getString(R.string.app_name);
        switch (displayMode){
            case SEARCH_MODE:
                navBarTitle += " - Search";
                break;
            case HISTORY_MODE:
                navBarTitle += " - History";
                break;
            default:
                break;
        }
        getSupportActionBar().setTitle(navBarTitle);
    }

    private void setList(String displayMode){
        switch (displayMode){
            case SEARCH_MODE:
                loadSearchList();
                break;
            case HISTORY_MODE:
                loadHistoryList();
                break;
            default:
                break;
        }
    }

    private void loadHistoryList(){
        List<Photo> savedPhotos = photoPersistenceManager.getAll();
        adapter.setPhotoList(savedPhotos);
    }

    private void loadSearchList(){
        adapter.setPhotoList(photoList);
        if(bound) {
            String perPage = drawerImagesNbSpinner.getSelectedItem().toString();
            String query = searchText.getText().toString();
            flickrService.getPhotoList(perPage, query);
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

            //List will be reloaded if activity is created, re-reacreated(after device rotation), or restarted (when the user press Back button in DetailActivity)
            setList(getDisplayMode());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Photo photo = adapter.getItem(position);
        Photo savedPhoto = photoPersistenceManager.getByUrl(photo.getUrl());
        if(savedPhoto!=null){
            photo = savedPhoto;
        }
        photo.setClickCounter(photo.getClickCounter()+1);
        photoPersistenceManager.save(photo);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(FULL_VIEW_PHOTO, photo);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        loadSearchList();
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
        //save preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DISPLAY_MODE, getDisplayModeByButtonIndex(value));
        editor.commit();
        //update display
        setNavBarTitle(getDisplayMode());
        setSearchLayoutVisibility(getDisplayMode());
        setList(getDisplayMode());
    }

    //drawer dropdown listeners
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //save preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(IMAGE_PER_PAGE_INDEX, position);
        editor.commit();
        //update display
        setList(getDisplayMode());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onRowDeleted(Photo photo) {
        if(getDisplayMode().equals(HISTORY_MODE)) {
            photoPersistenceManager.delete(photo);
        }
    }
}
