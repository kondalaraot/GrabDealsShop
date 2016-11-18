package com.grabdeals.shop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.grabdeals.shop.R;
import com.grabdeals.shop.util.APIParams;
import com.grabdeals.shop.util.Constants;
import com.grabdeals.shop.util.NetworkManager;
import com.grabdeals.shop.util.VolleyCallbackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseAppCompatActivity  implements VolleyCallbackListener{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
//    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mPhoneNoView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mPhoneNoView = (EditText) findViewById(R.id.phone_no);
//        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.btn_sign_in);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mForgotPassButton = (Button) findViewById(R.id.btn_forgot_pass);
        mForgotPassButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,PostOfferActivity.class));
            }
        });

        Button mCreateAcc = (Button) findViewById(R.id.btn_create_acc);
        mCreateAcc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterShopKeeperActivity.class));
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        /*if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
        mPhoneNoView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String poneNo = mPhoneNoView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid mobile number.
        if (TextUtils.isEmpty(poneNo)) {
            mPhoneNoView.setError(getString(R.string.error_field_required));
            focusView = mPhoneNoView;
            cancel = true;
        } else if (!isValidMobile(poneNo)) {
            mPhoneNoView.setError(getString(R.string.error_invalid_mobile));
            focusView = mPhoneNoView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress("please wait...");
            NetworkManager.getInstance().postRequest(Constants.API_LOGIN,getParams(),this);


        }
    }


    private boolean isValidMobile(String phone)
    {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private Map<String, String> getParams(){
        Map<String, String> jsonParams = new HashMap<>();

        try {
            jsonParams.put(APIParams.PARAM_MOBILE_NO, mPhoneNoView.getText().toString());
            jsonParams.put(APIParams.PARAM_PASSWORD, mPasswordView.getText().toString());

        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonParams;
    }

    @Override
    public void getResult(Object object) {
        dismissProgress();
        try {
            JSONObject jsonObject = (JSONObject) object;

            if (jsonObject!=null && jsonObject.getInt("code") == 200) {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject account = data.getJSONObject("account");
                String shopID = account.getString("shop_id");
                Intent intent = new Intent(this,EnterShopDetailsActivity.class);
                intent.putExtra("ARG_SHOP_ID",shopID);
                startActivity(intent);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getErrorResult(Object errorResp) {
        dismissProgress();
        if(errorResp !=null){
            showAlert((String) errorResp);
        }


    }
}

