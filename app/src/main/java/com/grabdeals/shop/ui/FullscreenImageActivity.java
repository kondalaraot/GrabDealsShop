package com.grabdeals.shop.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.grabdeals.shop.R;
import com.grabdeals.shop.adapter.FullScreenSlidePagerAdapter;

import java.util.ArrayList;

public class FullscreenImageActivity extends BaseAppCompatActivity {

    int pos;
    ArrayList<String> attachmentUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pos = getIntent().getIntExtra("POS",0);
        attachmentUrls = getIntent().getStringArrayListExtra("ImagesList");
        initImageSlider();

    }

    private void initImageSlider() {
       /* final ArrayList<String> attachmentUrls= new ArrayList<String>();
        final List<Attachment> attachments = mOffer.getAttachments();
        for (Attachment attachment : attachments) {
            attachmentUrls.add(Constants.SHOP_OFFER_AVATAR_URL+attachment.getImage_path()+".png");
        }*/
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        FullScreenSlidePagerAdapter pagerAdapter =new FullScreenSlidePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addAll(attachmentUrls);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(pos);

        //Resmin altındaki kucuk yuvarlak iconları resim saysına göre üreten CirclePageIndicator sınıfını cagırdık
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);


    }

}
