package com.syz.magicbox.magicbox.domain;

/**
 * Created by chx on 2017/2/27.
 */

public class LocalImage {
    private String name;
    private int imageId;

    public LocalImage(String name,int id){
        this.name = name;
        this.imageId = id;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }
}
