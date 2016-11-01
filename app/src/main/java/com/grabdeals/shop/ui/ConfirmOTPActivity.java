package com.grabdeals.shop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.grabdeals.shop.R;
import com.grabdeals.shop.util.APIParams;
import com.grabdeals.shop.util.Constants;
import com.grabdeals.shop.util.NetworkImageViewRounded;
import com.grabdeals.shop.util.NetworkManager;
import com.grabdeals.shop.util.VolleyCallbackListener;

import java.util.HashMap;
import java.util.Map;

public class ConfirmOTPActivity extends BaseAppCompatActivity implements View.OnClickListener,VolleyCallbackListener{

    private static final String TAG = "";

    private NetworkImageViewRounded mImage;
    private EditText mEnterOtp;
    private Button mBtnVerify;
    private Button mBtnResendOtp;

    private String mobileNo;
    private String shopName;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_otp);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        mobileNo = getIntent().getStringExtra("KEY_MOBILE_NO");
        password = getIntent().getStringExtra("KEY_PASSWORD");
        shopName = getIntent().getStringExtra("KEY_SHOP_NAME");
        findViews();

    }
    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-10-29 23:16:31 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        mImage = (NetworkImageViewRounded)findViewById( R.id.image );
        mEnterOtp = (EditText)findViewById( R.id.enter_otp );
        mBtnVerify = (Button)findViewById( R.id.btn_verify );
        mBtnResendOtp = (Button)findViewById( R.id.btn_resend_otp );
        mImage.setDefaultImageResId(R.drawable.office_building_icon);
        mBtnVerify.setOnClickListener( this );
        mBtnResendOtp.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2016-10-29 23:16:31 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == mBtnVerify ) {
            // Handle clicks for mBtnVerify
            NetworkManager.getInstance().postRequest(Constants.API_SIGN_UP,preparePostParams(),this);
        } else if ( v == mBtnResendOtp ) {
            // Handle clicks for mBtnResendOtp
        }
    }

    private Map<String,String> preparePostParams(){
        Map<String, String> formParams = new HashMap<>();
        formParams.put(APIParams.PARAM_MOBILE_NO, mobileNo);
        formParams.put(APIParams.PARAM_SHOP_NAME, shopName);
        formParams.put(APIParams.PARAM_PASSWORD, password);
        formParams.put(APIParams.PARAM_OTP_CODE, mEnterOtp.getText().toString());
        return formParams;
    }

    @Override
    public void getResult(Object object) {

        startActivity(new Intent(this,EnterShopDetailsActivity.class));

    }

    @Override
    public void getErrorResult(Object object) {

    }
}
