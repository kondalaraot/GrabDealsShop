package com.grabdeals.shop.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.grabdeals.shop.R;
import com.grabdeals.shop.util.NetworkImageViewRounded;

public class ConfirmOTPActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "";

    private NetworkImageViewRounded mImage;
    private EditText mEnterOtp;
    private Button mBtnVerify;
    private Button mBtnResendOtp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_otp);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


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
        mImage.setDefaultImageResId(R.drawable.default_user);
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
        } else if ( v == mBtnResendOtp ) {
            // Handle clicks for mBtnResendOtp
        }
    }
}
