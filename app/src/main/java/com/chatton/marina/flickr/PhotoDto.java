package com.chatton.marina.flickr;

/**
 * Created by Marina on 25/11/2016.
 */

public class PhotoDto {
    private String id;
    private String owner;
    private String secret;
    private String server;
    private long farm;
    private String title;
    private long ispublic;
    private long isfriend;
    private long isfamily;

    //required by Java Bean convention
    //Dto for Data Transfert Object, "raw" format matching the API response
    public PhotoDto() {
    }

    public PhotoDto(String id, String owner, String secret, String server, long farm, String title, long ispublic, long isfriend, long isfamily) {
        this.id = id;
        this.owner = owner;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
        this.title = title;
        this.ispublic = ispublic;
        this.isfriend = isfriend;
        this.isfamily = isfamily;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public long getFarm() {
        return farm;
    }

    public void setFarm(long farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getIspublic() {
        return ispublic;
    }

    public void setIspublic(long ispublic) {
        this.ispublic = ispublic;
    }

    public long getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(long isfriend) {
        this.isfriend = isfriend;
    }

    public long getIsfamily() {
        return isfamily;
    }

    public void setIsfamily(long isfamily) {
        this.isfamily = isfamily;
    }
}
