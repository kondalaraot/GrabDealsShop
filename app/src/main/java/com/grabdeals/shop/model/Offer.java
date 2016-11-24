package com.grabdeals.shop.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KTirumalsetty on 11/17/2016.
 */

public class Offer implements Serializable{

    /*"created_date": "2016-11-22 17:54:52",
            "offer_end": "2016-11-23",
            "title": "offer title test flat 30 % discount",
            "offer_id": "4",
            "locations": [
    {
        "city_id": "2",
            "city_name": "Hyderabad",
            "area_id": "3",
            "area_name": "Ameerpet"
    },
    {
        "city_id": "2",
            "city_name": "Hyderabad",
            "area_id": "70",
            "area_name": "Madhapur"
    }
    ],
            "description": "Offer description of the day please get 40 to 50 percent of the day please get 40 to 50 percent of the day please get 40 to 50 percent of the day please get 40 to 50 percent \nThanks for the apps worked earlier this week and",
            "offer_start": "2016-11-22",
            "attachments": [

            ]*/

    private String created_date;
    private String offer_end;
    private String title;
    private String offer_id;
    private String description;
    private String offer_start;
    List<Location> locations;
    List<String> attachments;

    public Offer() {
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getOffer_end() {
        return offer_end;
    }

    public void setOffer_end(String offer_end) {
        this.offer_end = offer_end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOffer_start() {
        return offer_start;
    }

    public void setOffer_start(String offer_start) {
        this.offer_start = offer_start;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }
}
