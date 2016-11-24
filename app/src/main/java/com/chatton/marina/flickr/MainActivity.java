package com.chatton.marina.flickr;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<Item> itemsList = new ArrayList<>();
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState==null) {
            itemsList.add(new Item("marguerite", "http://www.coloori.com/wp-content/uploads/2016/04/marguerite.jpg"));
            itemsList.add(new Item("rose", "http://www.lejardindesfleurs.com/img/cms/A-rose-is-a-rose-roses-20581060-2256-1496.jpg"));
            itemsList.add(new Item("pivoine", "https://static.pratique.fr/images/unsized/pi/pivoine-rouge.jpg"));
        }else{
            itemsList = (List<Item>) savedInstanceState.getSerializable("list");
        }

        adapter = new ListAdapter(this);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        adapter.setItemsList(itemsList);

        listView.setOnItemClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putSerializable("list", (Serializable) itemsList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Item item = adapter.getItem(position);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("item", item);
        startActivity(intent);
    }
}
