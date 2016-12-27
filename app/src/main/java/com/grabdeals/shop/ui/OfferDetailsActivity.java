package com.grabdeals.shop.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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
import com.grabdeals.shop.model.Attachment;
import com.grabdeals.shop.model.Location;
import com.grabdeals.shop.model.Offer;
import com.grabdeals.shop.util.APIParams;
import com.grabdeals.shop.util.ClickableViewPager;
import com.grabdeals.shop.util.Constants;
import com.grabdeals.shop.util.NetworkManager;
import com.grabdeals.shop.util.NetworkUtil;
import com.grabdeals.shop.util.VolleyCallbackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfferDetailsActivity extends BaseAppCompatActivity implements VolleyCallbackListener{

    private static final String TAG = "OfferDetailsActivity";

//    private NetworkImageViewRounded mIvShop;
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
//            Location location = mOffer.getLocations().get(0);
            String offerLocations ="";
            if(mOffer.getLocations()!=null && mOffer.getLocations().size()>0){
                StringBuilder builder = new StringBuilder();
                for (Location location : mOffer.getLocations()) {
                    builder.append(location.getArea_name()+",");
                }
                String locations = builder.toString();
                offerLocations = locations.substring(0,locations.length()-1);
                mTvOfferAddress.setText(offerLocations);

            }else{
                mTvOfferAddress.setText("");

            }
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
//        mIvShop.setDefaultImageResId(R.drawable.default_user);
//        mIvShop.setImageUrl(shorUrl,NetworkManager.getInstance().getImageLoader());
    }

    private void initImageSlider() {
        final ArrayList<String> attachmentUrls= new ArrayList<String>();
        final List<Attachment> attachments = mOffer.getAttachments();
        for (Attachment attachment : attachments) {
            attachmentUrls.add(Constants.SHOP_OFFER_AVATAR_URL+attachment.getImage_path()+".png");
        }
        ClickableViewPager pager = (ClickableViewPager) findViewById(R.id.pager);
        ScreenSlidePagerAdapter pagerAdapter =new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addAll(attachmentUrls);
        pager.setAdapter(pagerAdapter);
        pager.setOnItemClickListener(new ClickableViewPager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // your code
                Intent intent = new Intent(OfferDetailsActivity.this,FullscreenImageActivity.class);
                intent.putExtra("POS",position);
                intent.putStringArrayListExtra("ImagesList",attachmentUrls);
                startActivity(intent);
            }
        });
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               /* Intent intent = new Intent(OfferDetailsActivity.this,FullscreenImageActivity.class);
                intent.putExtra("POS",position);
                intent.putStringArrayListExtra("ImagesList",attachmentUrls);
                startActivity(intent);*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

        if (item.getItemId() == R.id.action_delete) {
            showDeleteAlertDialog();
        }else  if (item.getItemId() == R.id.action_edit) {
//            showAlertDialog();
            if(mOffer !=null){
                Intent i=new Intent(this,PostOfferActivity.class);
                i.putExtra("Type", Constants.EDIT_OFFER);
                i.putExtra("OfferObj", mOffer);
                startActivity(i);
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete offer?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(NetworkUtil.isNetworkAvailable(OfferDetailsActivity.this)){
                            showProgress("Deleting offer..");
                            Map<String,String> map = new HashMap<String, String>();
                            map.put(APIParams.PARAM_OFFER_ID,mOffer.getOffer_id());
                            NetworkManager.getInstance().postRequest(Constants.API_SHOP_DELETE_OFFER,map,OfferDetailsActivity.this,Constants.API_SHOP_DELETE_OFFER_REQ_CODE);
                        }else {
                            showAlert("Please check network connection..");
                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public void getResult(int reqCode,Object object) {
        dismissProgress();
        try {
            JSONObject response = (JSONObject) object;
            if(reqCode == Constants.API_SHOP_DELETE_OFFER_REQ_CODE){
                if (response!=null && response.getInt("code") == 200) {
                    Intent intent = new Intent(this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                } else {
                    showAlert(response.getString("message"));

                }
            }else{
                if (response!=null && response.getInt("code") == 200) {
                    JSONObject data = response.getJSONObject("data");
                    Gson gson = new Gson();
//                Type listType = new TypeToken<List<Offer>>(){}.getType();
                    Offer offerDetails = gson.fromJson(data.toString(), Offer.class);

                } else {
                    showAlert(response.getString("message"));

                }
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
