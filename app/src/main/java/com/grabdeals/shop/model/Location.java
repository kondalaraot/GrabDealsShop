package com.grabdeals.shop.model;

import java.io.Serializable;

/**
 * Created by KTirumalsetty on 11/24/2016.
 */

public class Location implements Serializable{
   /* "city_id": "2",
            "city_name": "Hyderabad",
            "area_id": "70",
            "area_name": "Madhapur"*/

    private String area_id;
    private String city_id;
    private String city_name;
    private String area_name;

    public Location() {
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }
}
