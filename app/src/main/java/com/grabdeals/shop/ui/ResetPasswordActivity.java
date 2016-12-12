package com.grabdeals.shop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.grabdeals.shop.R;
import com.grabdeals.shop.util.APIParams;
import com.grabdeals.shop.util.Constants;
import com.grabdeals.shop.util.NetworkManager;
import com.grabdeals.shop.util.NetworkUtil;
import com.grabdeals.shop.util.VolleyCallbackListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends BaseAppCompatActivity  implements View.OnClickListener,VolleyCallbackListener {

    private TextView mOtpSentTo;
    private EditText mEnterOtp;
    private EditText mEtPassword;
    private Button mBtnReset;
    private Button mBtnResendOtp;
    private String mMobileNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mMobileNo = getIntent().getStringExtra("KEY_MOBILE_NO");
        findViews();

    }


    private void findViews() {
        mOtpSentTo = (TextView)findViewById( R.id.otp_sent_to );
        mEnterOtp = (EditText)findViewById( R.id.enter_otp );
        mEtPassword = (EditText)findViewById( R.id.et_password );
        mBtnReset = (Button)findViewById( R.id.btn_reset );
        mBtnResendOtp = (Button)findViewById( R.id.btn_resend_otp );

        mBtnReset.setOnClickListener( this );
        mBtnResendOtp.setOnClickListener( this );
        mOtpSentTo.append(mMobileNo);
    }


    @Override
    public void onClick(View v) {
        if ( v == mBtnReset ) {
            // Handle clicks for mBtnReset
            if(validate()){
                if(NetworkUtil.isNetworkAvailable(this)){
                    showProgress("Setting new password..");
                    NetworkManager.getInstance().postRequest(Constants.API_SHOP_SET_PASSWORD,prepareSendOtpPostParams(),this,Constants.API_SHOP_SET_PASSWORD_REQ_CODE);

                }else{
                    showAlert("Please check your network..");
                }
            }
        } else if ( v == mBtnResendOtp ) {
            if(NetworkUtil.isNetworkAvailable(this)){
                showProgress("Resending OTP...");
                NetworkManager.getInstance().postRequest(Constants.API_SEND_OTP,prepareSendOtpPostParams(),this,Constants.API_SEND_OTP_REQ_CODE);
            }else{
                showAlert("Please check your network..");
            }
        }
    }

    private boolean validate() {
        boolean isValid = true;
        boolean cancel = false;
        View focusView = null;
        // Store values at the time of the login attempt.

//        String password = mPasswordView.getText().toString();
        // Check for a valid mobile number.
        if(hasText(mEnterOtp)){
            focusView = mEnterOtp;
            cancel = true;
        }else if(hasText(mEtPassword)){
            focusView = mEtPassword;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            isValid = false;
        } else {
            isValid = true;
        }
        return isValid;
    }

    private Map<String,String> prepareSendOtpPostParams(){
        Map<String, String> formParams = new HashMap<>();
        formParams.put(APIParams.PARAM_MOBILE_NO, mMobileNo);
        formParams.put(APIParams.PARAM_PASSWORD, mEtPassword.getText().toString());
        formParams.put(APIParams.PARAM_OTP_CODE, mEnterOtp.getText().toString());

        return formParams;
    }
    @Override
    public void getResult(int reqCode, Object object) {
        dismissProgress();
        JSONObject response = (JSONObject) object;
        switch (reqCode){
            case Constants.API_SHOP_SET_PASSWORD_REQ_CODE:
                try {
                    if(response.getInt("code") == 200){
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(new Intent(this,LoginActivity.class));
                    }else{
                        showAlert(response.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case Constants.API_SEND_OTP_REQ_CODE:
                try {
                    if(response.getInt("code") == 200){
                        showAlert(response.getString("message"));
                    }else{
                        showAlert(response.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    @Override
    public void getErrorResult(Object object) {
        dismissProgress();
        showAlert((String) object);
    }
}
