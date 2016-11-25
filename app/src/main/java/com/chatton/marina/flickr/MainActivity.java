package com.chatton.marina.flickr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private List<Photo> photoList = new ArrayList<>();
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(savedInstanceState==null) {
            photoList.add(new Photo("marguerite", "http://www.coloori.com/wp-content/uploads/2016/04/marguerite.jpg"));
            photoList.add(new Photo("rose", "http://www.lejardindesfleurs.com/img/cms/A-rose-is-a-rose-roses-20581060-2256-1496.jpg"));
            photoList.add(new Photo("pivoine", "https://static.pratique.fr/images/unsized/pi/pivoine-rouge.jpg"));
            photoList.add(new Photo("rose trémière", "http://a407.idata.over-blog.com/3/72/39/91/Rose-tremiere/056.JPG"));
            photoList.add(new Photo("rose d'Inde", "http://www.rustica.fr/images/308516j-l720-h512.jpg"));
            photoList.add(new Photo("oeillet d'Inde", "http://media.ooreka.fr/public/image/plant/36/varietyImage/cu90y4mrztskc0gk44o0oc4k0-source-9498396.jpg"));
            photoList.add(new Photo("oeillet", "http://www.papillonsdemots.fr/wp-content/uploads/2012/06/Oeillet-rouge.jpg"));
            photoList.add(new Photo("oeillet de poète", "http://a405.idata.over-blog.com/600x450/3/01/28/31/jardin-2/oeillets-de-po-te--800x600---2-.jpg"));
            photoList.add(new Photo("iris", "http://maxpull.gdvuch3veo.netdna-cdn.com/wp-content/uploads/2012/05/Siberian-iris.jpg"));
            photoList.add(new Photo("crocus", "http://wallpaperlayer.com/img/2015/2/crocuses-wallpaper-1343-1474-hd-wallpapers.jpg"));
            photoList.add(new Photo("bougainvilliers", "http://media.ooreka.fr/public/image/plant/53/mainImage-full-10233175.jpg"));
            photoList.add(new Photo("glycine", "https://static.pratique.fr/images/unsized/gl/glycine-chine-culture.jpg"));
        }else{
            photoList = (List<Photo>) savedInstanceState.getSerializable("list");
        }

        adapter = new ListAdapter(this);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        adapter.setPhotoList(photoList);

        listView.setOnItemClickListener(this);

        ImageButton searchButton = (ImageButton) findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putSerializable("list", (Serializable) photoList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Photo photo = adapter.getItem(position);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("photo", photo);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        final EditText searchText = (EditText) findViewById(R.id.search_text);
        Toast.makeText(MainActivity.this, searchText.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}
