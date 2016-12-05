package com.grabdeals.shop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.grabdeals.shop.R;
import com.grabdeals.shop.adapter.ScreenSlidePagerAdapter;
import com.grabdeals.shop.model.Location;
import com.grabdeals.shop.model.Offer;
import com.grabdeals.shop.util.Constants;
import com.grabdeals.shop.util.NetworkImageViewRounded;
import com.grabdeals.shop.util.NetworkManager;
import com.grabdeals.shop.util.NetworkUtil;
import com.grabdeals.shop.util.VolleyCallbackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class OfferDetailsActivity extends BaseAppCompatActivity implements VolleyCallbackListener{

    private static final String TAG = "OfferDetailsActivity";

    private NetworkImageViewRounded mIvShop;
    private TextView mTvShopName;
    private TextView mTvOfferTitle;
    private TextView mTvOfferAddress;
    private TextView mTvOfferPhoneNo;
    private TextView mTvShopUrl;
    private TextView mTvOfferTimings;
    private TextView mTvOfferEndDate;
    private TextView mTvOfferDesc;
    private ImageView mIvOffer;

    private String mOfferID;
    private Offer mOffer;
    private static final String[] IMAGES = new String[] {
            "http://tugbaustundag.com/slider/Android_Developer_Days_Logo.png",
            "http://tugbaustundag.com/slider/cocuklar-icin-bilisim-zirvesi.jpg",
            "http://tugbaustundag.com/slider/indirmobil700.jpg",
            "http://tugbaustundag.com/slider/ux700.png"

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mOfferID = getIntent().getStringExtra("OFFER_ID");
        mOffer = (Offer) getIntent().getSerializableExtra("OFFER_OBJ");
        findViews();
        if(mOffer == null){
             if(NetworkUtil.isNetworkAvailable(this)){
            showProgress("Please wait, fetching offer details...");
            NetworkManager.getInstance().getRequest(Constants.API_OFFER_DETAILS_BY_ID+mOfferID,null,this,0);
//            getPatientsVolleyStringReq();
        }else{
            showAlert("Please check your network connection..");
        }
        }else{
            populateOfferDetails();
        }


    }

    private void populateOfferDetails() {
        try {
            mTvShopName.setText(getPrefManager().getShopName());
            mTvOfferTitle.setText(mOffer.getTitle());
            Location location = mOffer.getLocations().get(0);
            mTvOfferAddress.setText(location.getArea_name()+","+location.getCity_name());
            mTvOfferPhoneNo.setText(getPrefManager().getShopMoBileNo());
            mTvShopUrl.setText(getPrefManager().getShopWebsite());
            mTvOfferTimings.setText("No data");
            mTvOfferEndDate.append(mOffer.getOffer_end());
            mTvOfferDesc.setText(mOffer.getDescription());
//        mIvOffer.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.office_building_icon, null));
//            mIvOffer.setDefaultImageResId(R.drawable.office_building_icon);
            String shorUrl = Constants.SHOP_AVATAR_URL+getPrefManager().getAccID()+"_"+getPrefManager().getShopID()+".png";
            Log.d(TAG,shorUrl);
            /*mIvOffer.setImageUrl(shorUrl,NetworkManager.getInstance().getImageLoader());
            mIvOffer.setErrorImageResId(android.R.drawable.ic_dialog_alert);*/

           /* ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
            imageLoader.displayImage(shorUrl, mIvOffer);*/
            initImageSlider();

        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    private void findViews() {
        mIvShop = (NetworkImageViewRounded)findViewById( R.id.iv_shop );
        mTvShopName = (TextView)findViewById( R.id.tv_shop_name );
        mTvOfferTitle = (TextView)findViewById( R.id.tv_offer_title );
        mTvOfferAddress = (TextView)findViewById( R.id.tv_offer_address );
        mTvOfferPhoneNo = (TextView)findViewById( R.id.tv_offer_phone_no );
        mTvShopUrl = (TextView)findViewById( R.id.tv_shop_url );
        mTvOfferTimings = (TextView)findViewById( R.id.tv_offer_timings );
        mTvOfferEndDate = (TextView)findViewById( R.id.tv_offer_end_date );
        mTvOfferDesc = (TextView)findViewById( R.id.tv_offer_desc );
//        mIvOffer = (ImageView) findViewById( R.id.iv_offer );
        String shorUrl = Constants.SHOP_AVATAR_URL+getPrefManager().getAccID()+"_"+getPrefManager().getShopID()+".png";
        Log.d(TAG,shorUrl);
        mIvShop.setDefaultImageResId(R.drawable.default_user);
        mIvShop.setImageUrl(shorUrl,NetworkManager.getInstance().getImageLoader());
    }

    private void initImageSlider() {
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        //Resimlermizi arayüzde göstermek için kullancagmız ScreenSlidePagerAdapter sınıfına resim, yollarnı set ettim.
        ScreenSlidePagerAdapter pagerAdapter =new ScreenSlidePagerAdapter(getSupportFragmentManager());

        pagerAdapter.addAll(Arrays.asList(IMAGES));
        pager.setAdapter(pagerAdapter);
        //Resmin altındaki kucuk yuvarlak iconları resim saysına göre üreten CirclePageIndicator sınıfını cagırdık
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_offer_details, menu);
        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_fav) {
//            showAlertDialog();
        }else  if (item.getItemId() == R.id.action_share) {
//            showAlertDialog();
            if(mOffer !=null){
                Intent i=new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(android.content.Intent.EXTRA_SUBJECT,mOffer.getTitle());
                i.putExtra(android.content.Intent.EXTRA_TEXT, mOffer.getDescription());
                startActivity(Intent.createChooser(i,"Share via"));
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getResult(int reqCode,Object object) {
        dismissProgress();
        try {
            JSONObject response = (JSONObject) object;

            if (response!=null && response.getInt("code") == 200) {
                JSONObject data = response.getJSONObject("data");
                Gson gson = new Gson();
//                Type listType = new TypeToken<List<Offer>>(){}.getType();
                Offer offerDetails = gson.fromJson(data.toString(), Offer.class);

            } else {
                showAlert(response.getString("message"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getErrorResult(Object object) {
        dismissProgress();
        if(object !=null){
            showAlert((String) object);
        }
    }
}
