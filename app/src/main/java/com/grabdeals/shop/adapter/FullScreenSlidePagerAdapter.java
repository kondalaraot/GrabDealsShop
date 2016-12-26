package com.grabdeals.shop.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.grabdeals.shop.fragment.FullScreenZoomFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suchi on 12/25/2016.
 */

public class FullScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private List<String> picList = new ArrayList<>();

    public FullScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        //Resimleri Image loader kütüphanesini kullanarak yüklenmesi icin resim url, ScreenSlidePageFragment sınıfına atadık.
        return FullScreenZoomFragment.newInstance(picList.get(i));
    }

    @Override
    public int getCount() {
        return picList.size();
    }

    public void addAll(List<String> picList) {
        this.picList = picList;
    }
}

