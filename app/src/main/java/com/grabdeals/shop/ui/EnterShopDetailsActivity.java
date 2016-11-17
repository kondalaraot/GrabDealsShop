package com.grabdeals.shop.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.grabdeals.shop.R;
import com.grabdeals.shop.util.Constants;
import com.grabdeals.shop.util.NetworkImageViewRounded;
import com.grabdeals.shop.util.NetworkManager;
import com.grabdeals.shop.util.NetworkUtil;
import com.grabdeals.shop.util.VolleyCallbackListener;

import java.util.HashMap;
import java.util.Map;

public class EnterShopDetailsActivity extends BaseAppCompatActivity implements View.OnClickListener,VolleyCallbackListener{


    private static final String TAG = "EnterShopDetailsActivity";

    private NetworkImageViewRounded mImage;
    private ImageView mIvCamera;
    private EditText mAboutShop;
    private Spinner mSpinnerCategory;
    private EditText mWebsite;
    private EditText mLocation;
    private EditText mFullAddress;
    private EditText mPhoneNumber;
    private Button mBtnAddMoreLoc;
    private Button mBtnSaveDetails;

    private int mShopCategoryPos;
    private String mShopCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_shop_details);
        findViews();

    }

    private void findViews() {
        mImage = (NetworkImageViewRounded)findViewById( R.id.image );
        mIvCamera = (ImageView)findViewById( R.id.iv_camera );
        mAboutShop = (EditText)findViewById( R.id.about_shop );
        mSpinnerCategory = (Spinner)findViewById( R.id.spinner_category );
        mWebsite = (EditText)findViewById( R.id.website );
        mLocation = (EditText)findViewById( R.id.location );
        mFullAddress = (EditText)findViewById( R.id.full_address );
        mPhoneNumber = (EditText)findViewById( R.id.phone_number );
        mBtnAddMoreLoc = (Button)findViewById( R.id.btn_add_more_loc );
        mBtnSaveDetails = (Button)findViewById( R.id.btn_save_details );

        mBtnAddMoreLoc.setOnClickListener( this );
        mBtnSaveDetails.setOnClickListener( this );

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.shop_categories, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        mSpinnerCategory.setAdapter(adapter);
        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
               // On selecting a spinner item
                mShopCategory = adapterView.getItemAtPosition(pos).toString();
                mShopCategoryPos = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        if ( v == mBtnAddMoreLoc ) {
            // Handle clicks for mBtnLogin
        } else if ( v == mBtnSaveDetails ) {
            // Handle clicks for mBtnSaveDetails
            if (validate())
            if(NetworkUtil.isNetworkAvailable(this)){
                showProgress("Please wait, fetching offers...");
                NetworkManager.getInstance().postRequest(Constants.API_ADD_SHOP,preparePostParams(),this);
            }else{
                showAlert("Please check your network connection..");
            }
        }
    }

    private boolean validate() {
        boolean isValid = true;
        boolean cancel = false;
        View focusView = null;
        // Store values at the time of the login attempt.
        String aboutShop = mAboutShop.getText().toString();
        String address = mFullAddress.getText().toString();
//        String password = mPasswordView.getText().toString();
        // Check for a valid mobile number.
        if(hasText(mAboutShop)){
            focusView = mAboutShop;
            cancel = true;
        }else if(hasText(mFullAddress)){
            focusView = mFullAddress;
            cancel = true;
        }
       /* if (TextUtils.isEmpty(aboutShop)) {
            mAboutShop.setError(getString(R.string.error_field_required));
            focusView = mAboutShop;
            cancel = true;
        } else  if (TextUtils.isEmpty(address)) {
            mFullAddress.setError(getString(R.string.error_field_required));
            focusView = mFullAddress;
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

    private Map<String,String> preparePostParams(){
        Map<String, String> formParams = new HashMap<>();
     /*   formParams.put(APIParams.PARAM_SHOP_NAME, mobileNo);
        formParams.put(APIParams.PARAM_SHOP_NAME, shopName);
        formParams.put(APIParams.PARAM_PASSWORD, password);
        formParams.put(APIParams.PARAM_OTP_CODE, mEnterOtp.getText().toString());
        formParams.put(APIParams.PARAM_FILE_DATA, FileUtils.convertBitmapToBase64(mShopImageBitmap));*/
        return formParams;
    }

    @Override
    public void getResult(Object object) {

    }

    @Override
    public void getErrorResult(Object object) {

    }
}
