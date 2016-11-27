package com.grabdeals.shop.util;

/**
 * Created by KTirumalsetty on 10/31/2016.
 */

public interface VolleyCallbackListener<T> {

    public void getResult(int reqCode,T object);
    public void getErrorResult(T object);
}
