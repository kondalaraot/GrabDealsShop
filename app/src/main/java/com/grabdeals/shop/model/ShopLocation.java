package com.grabdeals.shop.model;

import java.io.Serializable;

/**
 * Created by Suchi on 12/25/2016.
 */

public class ShopLocation implements Serializable{

    private int shopLocationId;
    private String shopLocationName;
    private String shopLocationFullAddress;
    private String shopLocationPhone;
    private double latitude;
    private double longitude;

    public ShopLocation() {
    }

    public int getShopLocationId() {
        return shopLocationId;
    }

    public void setShopLocationId(int shopLocationId) {
        this.shopLocationId = shopLocationId;
    }

    public String getShopLocationName() {
        return shopLocationName;
    }

    public void setShopLocationName(String shopLocationName) {
        this.shopLocationName = shopLocationName;
    }

    public String getShopLocationFullAddress() {
        return shopLocationFullAddress;
    }

    public void setShopLocationFullAddress(String shopLocationFullAddress) {
        this.shopLocationFullAddress = shopLocationFullAddress;
    }

    public String getShopLocationPhone() {
        return shopLocationPhone;
    }

    public void setShopLocationPhone(String shopLocationPhone) {
        this.shopLocationPhone = shopLocationPhone;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
