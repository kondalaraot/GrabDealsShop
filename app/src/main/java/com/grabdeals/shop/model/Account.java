package com.grabdeals.shop.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KTirumalsetty on 11/4/2016.
 */

public class Account implements Serializable{

    private String shop_description;
    private String category_name;
    private String shop_id;
    private String register_date;
    private String category_id;
    private String web_site;
    private String acc_id;
    private String shop_name;
    private String mobile_no;
    private List<ShopBranch> shop_branches;

    public Account() {
    }

    public String getShop_description() {
        return shop_description;
    }

    public void setShop_description(String shop_description) {
        this.shop_description = shop_description;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getRegister_date() {
        return register_date;
    }

    public void setRegister_date(String register_date) {
        this.register_date = register_date;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getWeb_site() {
        return web_site;
    }

    public void setWeb_site(String web_site) {
        this.web_site = web_site;
    }

    public String getAcc_id() {
        return acc_id;
    }

    public void setAcc_id(String acc_id) {
        this.acc_id = acc_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public List<ShopBranch> getShop_branches() {
        return shop_branches;
    }

    public void setShop_branches(List<ShopBranch> shop_branches) {
        this.shop_branches = shop_branches;
    }
}
