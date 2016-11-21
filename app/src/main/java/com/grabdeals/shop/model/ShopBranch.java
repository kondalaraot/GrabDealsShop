package com.grabdeals.shop.model;

import java.io.Serializable;

/**
 * Created by KTirumalsetty on 11/21/2016.
 */

public class ShopBranch implements Serializable {

    private String city_name;
    private String location_name;
    private String shop_state_id;
    private String shop_address;
    private String state_name;
    private String country_name;
    private String id;
    private String shop_country_id;
    private String shop_area_id;
    private String shop_city_id;
    private String area_name;
    private String longitude;
    private String latitude;
    private String shop_phone;

    public ShopBranch() {
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getShop_state_id() {
        return shop_state_id;
    }

    public void setShop_state_id(String shop_state_id) {
        this.shop_state_id = shop_state_id;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShop_country_id() {
        return shop_country_id;
    }

    public void setShop_country_id(String shop_country_id) {
        this.shop_country_id = shop_country_id;
    }

    public String getShop_area_id() {
        return shop_area_id;
    }

    public void setShop_area_id(String shop_area_id) {
        this.shop_area_id = shop_area_id;
    }

    public String getShop_city_id() {
        return shop_city_id;
    }

    public void setShop_city_id(String shop_city_id) {
        this.shop_city_id = shop_city_id;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getShop_phone() {
        return shop_phone;
    }

    public void setShop_phone(String shop_phone) {
        this.shop_phone = shop_phone;
    }
}
