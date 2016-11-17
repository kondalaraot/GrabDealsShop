package com.grabdeals.shop.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.grabdeals.shop.R;
import com.grabdeals.shop.model.Offer;
import com.grabdeals.shop.util.Constants;
import com.grabdeals.shop.util.NetworkManager;
import com.grabdeals.shop.util.NetworkUtil;
import com.grabdeals.shop.util.VolleyCallbackListener;

import org.json.JSONException;
import org.json.JSONObject;

public class OfferDetailsActivity extends BaseAppCompatActivity implements VolleyCallbackListener{

    private static final String TAG = "OfferDetailsActivity";

    private ImageView mIvShop;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mOfferID = getIntent().getStringExtra("OFFER_ID");
        findViews();

        if(NetworkUtil.isNetworkAvailable(this)){
            showProgress("Please wait, fetching offer details...");
            NetworkManager.getInstance().getRequest(Constants.API_OFFER_DETAILS_BY_ID+mOfferID,null,this);
//            getPatientsVolleyStringReq();
        }else{
            showAlert("Please check your network connection..");
        }


    }

    private void findViews() {
        mIvShop = (ImageView)findViewById( R.id.iv_shop );
        mTvShopName = (TextView)findViewById( R.id.tv_shop_name );
        mTvOfferTitle = (TextView)findViewById( R.id.tv_offer_title );
        mTvOfferAddress = (TextView)findViewById( R.id.tv_offer_address );
        mTvOfferPhoneNo = (TextView)findViewById( R.id.tv_offer_phone_no );
        mTvShopUrl = (TextView)findViewById( R.id.tv_shop_url );
        mTvOfferTimings = (TextView)findViewById( R.id.tv_offer_timings );
        mTvOfferEndDate = (TextView)findViewById( R.id.tv_offer_end_date );
        mTvOfferDesc = (TextView)findViewById( R.id.tv_offer_desc );
        mIvOffer = (ImageView)findViewById( R.id.iv_offer );
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getResult(Object object) {
        dismissProgress();
        try {
            JSONObject response = (JSONObject) object;

            if (response!=null && response.getInt("code") == 200) {
                JSONObject offerDetailsObj = response.getJSONObject("Content");
                Gson gson = new Gson();
//                Type listType = new TypeToken<List<Offer>>(){}.getType();
                Offer offerDetails = gson.fromJson(offerDetailsObj.toString(), Offer.class);

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
