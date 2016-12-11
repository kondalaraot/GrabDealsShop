package com.grabdeals.shop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.grabdeals.shop.R;
import com.grabdeals.shop.util.VolleyCallbackListener;

public class ResetPasswordActivity extends BaseAppCompatActivity  implements View.OnClickListener,VolleyCallbackListener{

    private EditText mEtMobileNo;
    private Button mBtnRequestOtp;
    private Button mBtnCreateAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    }


    private void findViews() {
        mEtMobileNo = (EditText)findViewById( R.id.et_mobile_no );
        mBtnRequestOtp = (Button)findViewById( R.id.btn_request_otp );
        mBtnCreateAcc = (Button)findViewById( R.id.btn_create_acc );

        mBtnRequestOtp.setOnClickListener( this );
        mBtnCreateAcc.setOnClickListener( this );
    }


    @Override
    public void onClick(View v) {
        if ( v == mBtnRequestOtp ) {
            // Handle clicks for

        } else if ( v == mBtnCreateAcc ) {
            // Handle clicks for mBtnResendOtp
            startActivity(new Intent(this,RegisterShopKeeperActivity.class));
        }
    }


    @Override
    public void getResult(int reqCode, Object object) {

    }

    @Override
    public void getErrorResult(Object object) {

    }
}
