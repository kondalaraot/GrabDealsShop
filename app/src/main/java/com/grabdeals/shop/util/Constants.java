package com.grabdeals.shop.util;

/**
 * Created by KTirumalsetty on 10/31/2016.
 */

public class Constants {
    public static final boolean DEBUG = true;

    public static final String HOST_URL = "http://tag.tollymovies.com/v1/";
//    public static final String SHOP_AVATAR_URL = "http://tag.tollymovies.com/v1/shop/avatar/shop_avatar_"; //Old
    public static final String SHOP_AVATAR_URL = "http://tag.tollymovies.com/assets/avatars/shops/shop_avatar_";
    public static final String SHOP_OFFER_AVATAR_URL = "http://tag.tollymovies.com/assets/offers/";

    public static final String API_IS_REGISTER = "shop/is_register";
    public static final String API_SIGN_UP = "shop/signup";
    public static final String API_LOGIN = "shop/login";
    public static final String API_ADD_SHOP = "shop/update/locations";
    public static final String API_POST_OFFER = "shop/post_offer";
    public static final String API_EDIT_OFFER = "shop/edit/offer";

    public static final String API_OFFER_ALL ="shop/offers/list";
    public static final String API_OFFER_DETAILS_BY_ID = "shop/offer/";
    public static final String API_SEND_OTP_SIGN_UP = "shop/send_otp/1";
    public static final String API_SEND_OTP = "shop/send_otp";
//    public static final String API_SHOP_LOCATIONS = "shop/locations/{shop_id}";
    public static final String API_SHOP_LOCATIONS = "shop/locations/";
    public static final String API_SHOP_UPLOAD_OFFER_ATTACHMENTS = "shop/upload/offer/attachments";
    public static final String API_SHOP_SET_PASSWORD = "shop/set_password";
    public static final String API_SHOP_DELETE_OFFER = "shop/delete/offer";

    public static final int API_SHOP_LOCATIONS_REQ_CODE = 4;
    public static final int API_SEND_OTP_SIGN_UP_REQ_CODE = 3;
    public static final int API_SEND_OTP_REQ_CODE = 1;
    public static final int API_SIGN_UP_REQ_CODE = 2;
    public static final int API_POST_OFFER_REQ_CODE = 5;
    public static final int API_SHOP_UPLOAD_OFFER_ATTACHMENTS_REQ_CODE = 6;
    public static final int API_SHOP_SET_PASSWORD_REQ_CODE = 7;
    public static final int API_SHOP_DELETE_OFFER_REQ_CODE = 8;
    public static final int API_EDIT_OFFER_REQ_CODE = 9;

    public static final String EDIT_OFFER = "Edit Offer";


}
