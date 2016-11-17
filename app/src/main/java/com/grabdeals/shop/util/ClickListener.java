package com.grabdeals.shop.util;

import android.view.View;

/**
 * Created by KTirumalsetty on 10/21/2016.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
