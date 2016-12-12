package com.grabdeals.shop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.grabdeals.shop.R;
import com.grabdeals.shop.util.APIParams;
import com.grabdeals.shop.util.Constants;
import com.grabdeals.shop.util.NetworkManager;
import com.grabdeals.shop.util.NetworkUtil;
import com.grabdeals.shop.util.VolleyCallbackListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestOtpActivity extends BaseAppCompatActivity  implements View.OnClickListener,VolleyCallbackListener{

    private EditText mEtMobileNo;
    private Button mBtnRequestOtp;
    private Button mBtnCreateAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_otp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViews();

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
            if(validate()){
                if(NetworkUtil.isNetworkAvailable(this)){
                    showProgress("Sending OTP...");
                    NetworkManager.getInstance().postRequest(Constants.API_SEND_OTP,prepareSendOtpPostParams(),this,Constants.API_SEND_OTP_REQ_CODE);
                }else{
                    showAlert("Please check your network..");
                }


            }

        } else if ( v == mBtnCreateAcc ) {
            // Handle clicks for mBtnResendOtp
            startActivity(new Intent(this,RegisterShopKeeperActivity.class));
            finish();
        }
    }

    private boolean validate() {
        boolean isValid = true;
        boolean cancel = false;
        View focusView = null;
        // Store values at the time of the login attempt.
        String mobileNo = mEtMobileNo.getText().toString();
//        String password = mPasswordView.getText().toString();
        // Check for a valid mobile number.
        if (TextUtils.isEmpty(mobileNo)) {
            mEtMobileNo.setError(getString(R.string.error_field_required));
            focusView = mEtMobileNo;
            cancel = true;
        } /*else if (!isValidOTP(poneNo)) {
            mPhoneNoView.setError(getString(R.string.error_invalid_mobile));
            focusView = mPhoneNoView;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            isValid = false;
        } else {
            isValid = true;
        }
        return isValid;
    }
    private Map<String,String> prepareSendOtpPostParams(){
        Map<String, String> formParams = new HashMap<>();
        formParams.put(APIParams.PARAM_MOBILE_NO, mEtMobileNo.getText().toString());

        return formParams;
    }

    @Override
    public void getResult(int reqCode, Object object) {
        dismissProgress();
        JSONObject response = (JSONObject) object;
        switch (reqCode){
            case Constants.API_SEND_OTP_REQ_CODE:
                try {
                    if(response.getInt("code") == 200){
                        Intent intent = new Intent(this,ResetPasswordActivity.class);
                        intent.putExtra("KEY_MOBILE_NO",mEtMobileNo.getText().toString());
                        startActivity(intent);
                    }else{
                        showAlert(response.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


        }
    }

    @Override
    public void getErrorResult(Object object) {
        dismissProgress();
        showAlert((String) object);

    }
}
