package com.chatton.marina.flickr;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FlickrService extends Service {
    private final IBinder binder = new ServiceBinder();
    List<Photo> photoList = new ArrayList<>();

    public List<Photo> getPhotoList() {
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

        return photoList;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class ServiceBinder extends Binder {
        FlickrService getService() {
            return FlickrService.this;
        }
    }
}
