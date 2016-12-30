package com.grabdeals.shop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.grabdeals.shop.R;
import com.grabdeals.shop.model.ShopLocation;

public class AddEditMoreLocationsActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final String TAG ="AddEditMoreLocationsAc" ;

    private EditText mLocation;
    private EditText mFullAddress;
    private EditText mPhoneNumber;
    private Button mBtnSaveDetails;

    private double mLatitude;
    private double mLongitude;

    private  ShopLocation mShopLocation;
    private int mPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_more_locations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mShopLocation = (ShopLocation) getIntent().getSerializableExtra("ShopLocationObj");
        mPosition = getIntent().getIntExtra("position",0);
        findViews();
        if(mShopLocation !=null){
            setTitle("Edit Shop Location");
            mBtnSaveDetails.setText("Update Details");
//            mBtnSaveDetails.setText();
            populateData();
        }

    }

    private void populateData() {
        mLocation.setText(mShopLocation.getShopLocationName());
        mFullAddress.setText(mShopLocation.getShopLocationFullAddress());
        mPhoneNumber.setText(mShopLocation.getShopLocationPhone());
        mLatitude = mShopLocation.getLatitude();
        mLongitude = mShopLocation.getLongitude();
    }

    private void findViews() {
        mLocation = (EditText)findViewById( R.id.location );
        mFullAddress = (EditText)findViewById( R.id.full_address );
        mPhoneNumber = (EditText)findViewById( R.id.phone_number );
        mBtnSaveDetails = (Button)findViewById( R.id.btn_save_details );
        mBtnSaveDetails.setOnClickListener(this);
        mLocation.setInputType(InputType.TYPE_NULL);

        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if ( v == mBtnSaveDetails ) {
            // Handle clicks for mBtnSaveDetails
            if (validate()){
               /* if(NetworkUtil.isNetworkAvailable(this)){
                    showProgress("Please wait, Adding Shop Details...");
                    NetworkManager.getInstance().postRequest(Constants.API_ADD_SHOP,preparePostParams(),this,0);
                }else{
                    showAlert("Please check your network connection..");
                }*/
                ShopLocation shopLocation = new ShopLocation();
                shopLocation.setShopLocationName(mLocation.getText().toString());
                shopLocation.setShopLocationFullAddress(mFullAddress.getText().toString());
                shopLocation.setShopLocationPhone(mPhoneNumber.getText().toString());
                shopLocation.setLatitude(mLatitude);
                shopLocation.setLongitude(mLongitude);
                Intent intent = new Intent();
                intent.putExtra("LocationObj",shopLocation);
                if(mShopLocation!=null){
                    intent.putExtra("EditItemPosition",mPosition);
                }
                setResult(RESULT_OK,intent);
                finish();

            }

        }
    }

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {

            case REQUEST_CODE_AUTOCOMPLETE:
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place Selected: " + place.getName());

                // Format the place's details and display them in the TextView.
                mLocation.setText(place.getName());
                       /* mLocation.setText(formatPlaceDetails(getResources(), place.getName(),
                                place.getId(), place.getAddress(), place.getPhoneNumber(),
                                place.getWebsiteUri()).toString());*/
                LatLng latLng = place.getLatLng();
                mLatitude = latLng.latitude;
                mLongitude = latLng.longitude;
                Log.d(TAG,"lat,long are "+mLatitude+"  "+mLongitude);
                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
//                            mLocation.setText(Html.fromHtml(attributions.toString()));
                    Log.d(TAG,"attributions "+attributions.toString());
                } else {
//                            mLocation.setText("");
                    Log.d(TAG,"attributions empty");
                }
                break;
            // Indicates that the activity closed before a selection was made. For example if
            // the user pressed the back button.
            case PlaceAutocomplete.RESULT_ERROR:
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
                break;

        }


    }

    private boolean validate() {
        boolean isValid = true;
        boolean cancel = false;
        View focusView = null;

       if(hasText(mLocation)){
            focusView = mLocation;
            cancel = true;
        }else if(hasText(mFullAddress)){
            focusView = mFullAddress;
            cancel = true;
        }else if(hasText(mPhoneNumber)){
            focusView = mPhoneNumber;
            cancel = true;
        }

        if (cancel) {
            // form field with an error.
            focusView.requestFocus();
            isValid = false;
        } else {
            isValid = true;
        }
        return isValid;
    }

}
