package com.grabdeals.shop.model;

import java.io.Serializable;

/**
 * Created by Suchi on 12/5/2016.
 */
public class Attachment implements Serializable{

    public Attachment() {
    }

    private String image_path;

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
