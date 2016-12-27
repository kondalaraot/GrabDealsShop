package com.grabdeals.shop.ui;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.grabdeals.shop.R;
import com.grabdeals.shop.model.ShopLocation;
import com.grabdeals.shop.util.APIParams;
import com.grabdeals.shop.util.Constants;
import com.grabdeals.shop.util.FileUtils;
import com.grabdeals.shop.util.ImageUtils;
import com.grabdeals.shop.util.NetworkImageViewRounded;
import com.grabdeals.shop.util.NetworkManager;
import com.grabdeals.shop.util.NetworkUtil;
import com.grabdeals.shop.util.VolleyCallbackListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnterShopDetailsActivity extends BaseAppCompatActivity implements View.OnClickListener, VolleyCallbackListener {

    private static final String TAG = "EnterShopDetailsAct";
    private static final int PICK_FROM_CAMERA = 10;
    private static final int CROP_FROM_CAMERA = 20;
    private static final int PICK_FROM_FILE = 30;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 40;
    private static final int REQUEST_ADD_MORE_LOCATIONS = 50;

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
    private LinearLayout mLLDynamicViews;

    private int mShopCategoryPos;
    private String mShopCategory;
    private Uri mImageCaptureUri;
    private Bitmap mShopImageBitmap;

    private String mShopID;

    double mLatitude, mLongitude;

    private ArrayList<ShopLocation> mShopLocations = new ArrayList<ShopLocation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShopID = getIntent().getStringExtra("ARG_SHOP_ID");
        setContentView(R.layout.activity_enter_shop_details);
        findViews();

    }

    private void findViews() {
        mImage = (NetworkImageViewRounded) findViewById(R.id.image);
        mIvCamera = (ImageView) findViewById(R.id.iv_camera);
        mAboutShop = (EditText) findViewById(R.id.about_shop);
        mSpinnerCategory = (Spinner) findViewById(R.id.spinner_category);
        mWebsite = (EditText) findViewById(R.id.website);
        mLocation = (EditText) findViewById(R.id.location);
        mFullAddress = (EditText) findViewById(R.id.full_address);
        mPhoneNumber = (EditText) findViewById(R.id.phone_number);
        mBtnAddMoreLoc = (Button) findViewById(R.id.btn_add_more_loc);
        mBtnSaveDetails = (Button) findViewById(R.id.btn_save_details);
        mLLDynamicViews = (LinearLayout) findViewById(R.id.ll_dynamic_locs);

        mBtnAddMoreLoc.setOnClickListener(this);
        mBtnSaveDetails.setOnClickListener(this);
        mIvCamera.setOnClickListener(this);
        String imageUrl = Constants.SHOP_AVATAR_URL + getPrefManager().getAccID() + "_" + getPrefManager().getShopID() + ".png";
        if (Constants.DEBUG) Log.d(TAG, "Shop image url" + imageUrl);
        ImageLoader mImageLoader = NetworkManager.getInstance(this)
                .getImageLoader();
        mImage.setDefaultImageResId(R.drawable.default_user);
        mImage.setImageUrl(imageUrl, mImageLoader);


        mLocation.setInputType(InputType.TYPE_NULL);

        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity();
            }
        });

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
        if (v == mBtnAddMoreLoc) {
            // Handle clicks for mBtnLogin
            startActivityForResult(new Intent(this, AddEditMoreLocationsActivity.class), REQUEST_ADD_MORE_LOCATIONS);

        } else if (v == mBtnSaveDetails) {
            // Handle clicks for mBtnSaveDetails
            if (validate()) {
                if (NetworkUtil.isNetworkAvailable(this)) {
                    showProgress("Please wait, Adding Shop Details...");
                    NetworkManager.getInstance().postRequest(Constants.API_ADD_SHOP, preparePostParams(), this, 0);
                } else {
                    showAlert("Please check your network connection..");
                }
            }

        } else if (v == mIvCamera) {
            selectImage();
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

    /**
     * Called after the autocomplete activity has finished to return its result.
     */


    /**
     * Helper method to format information about a place nicely.
     */
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.d(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, REQUEST_CAMERA);*/
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    mImageCaptureUri = Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), "shop_avatar_"
                            + String.valueOf(System.currentTimeMillis()) + ".jpg"));

                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                            mImageCaptureUri);

                    try {
                        intent.putExtra("return-data", true);

                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    //intent.setType("image/*");
                    //intent.setAction(Intent.);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                doCrop();
                break;

            case PICK_FROM_FILE:
                mImageCaptureUri = data.getData();
                doCrop();
                break;

            case CROP_FROM_CAMERA:
                Bundle extras = data.getExtras();

                if (extras != null) {
                    mShopImageBitmap = extras.getParcelable("data");
                    Bitmap mShopImageCircledBitmap = ImageUtils.getCircularBitmapWithWhiteBorder(this, mShopImageBitmap, 8);
//                    mImage.setImageBitmap(mShopImageBitmap);
//                    mImage.set(mShopImageBitmap);
                    mImage.setLocalImageBitmap(mShopImageCircledBitmap);

                }

                // File f = new File(mImageCaptureUri.getPath());
                //
                // if (f.exists())
                // f.delete();

                break;
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
                Log.d(TAG, "lat,long are " + mLatitude + "  " + mLongitude);
                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
//                            mLocation.setText(Html.fromHtml(attributions.toString()));
                    Log.d(TAG, "attributions " + attributions.toString());
                } else {
//                            mLocation.setText("");
                    Log.d(TAG, "attributions empty");
                }
                break;
            // Indicates that the activity closed before a selection was made. For example if
            // the user pressed the back button.
            case PlaceAutocomplete.RESULT_ERROR:
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
                break;
            case REQUEST_ADD_MORE_LOCATIONS:
                ShopLocation shopLocation = (ShopLocation) data.getSerializableExtra("LocationObj");
                mShopLocations.add(shopLocation);
                addLocationView(shopLocation);
//                Log.e(TAG, "Error: Status = " + status.toString());
                break;

        }


    }


    private void doCrop() {

        Log.d(TAG, "mImageCaptureUri---- " + mImageCaptureUri);
        // call the standard crop action intent
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        // indicate image type and Uri of image
        cropIntent.setDataAndType(mImageCaptureUri, "image/*");
        // set crop properties
        cropIntent.putExtra("crop", "true");
        // indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        // indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        // retrieve data on return
        cropIntent.putExtra("return-data", true);
        // start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, CROP_FROM_CAMERA);

    }

    private boolean validate() {
        boolean isValid = true;
        boolean cancel = false;
        View focusView = null;
        // Store values at the time of the login attempt.

//        String password = mPasswordView.getText().toString();
        // Check for a valid mobile number.
        if (hasText(mAboutShop)) {
            focusView = mAboutShop;
            cancel = true;
        } else if (hasSpinnerSelected(mSpinnerCategory)) {
            focusView = mSpinnerCategory;
            cancel = true;
        }/*else if(hasText(mWebsite)){
            focusView = mWebsite;
            cancel = true;
        }*/ else if (hasText(mLocation)) {
            focusView = mLocation;
            cancel = true;
        } else if (hasText(mFullAddress)) {
            focusView = mFullAddress;
            cancel = true;
        } else if (hasText(mPhoneNumber)) {
            focusView = mPhoneNumber;
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

    private Map<String, String> preparePostParams() {
        Map<String, String> formParams = new HashMap<>();
        formParams.put(APIParams.PARAM_SHOP_ID, getPrefManager().getShopID());
        formParams.put(APIParams.PARAM_ABOUT_SHOP, mAboutShop.getText().toString());
        formParams.put(APIParams.PARAM_CATEGORY_ID, mSpinnerCategory.getSelectedItem().toString());
        formParams.put(APIParams.PARAM_WEB_SITE, mWebsite.getText().toString());
        formParams.put(APIParams.PARAM_LOCATION_INFO, prepareLocationsInfo());
        if (mShopImageBitmap != null)
            formParams.put(APIParams.PARAM_FILE_DATA, FileUtils.convertBitmapToBase64(mShopImageBitmap));
        return formParams;
    }

    private String prepareLocationsInfo() {
        JSONArray jsonArray = new JSONArray();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("location_name", mLocation.getText().toString());
            jsonObject.put("full_address", mFullAddress.getText().toString());
            jsonObject.put("phone_no", mPhoneNumber.getText().toString());
            jsonObject.put("latitude", mLatitude);
            jsonObject.put("longitude", mLongitude);
            jsonArray.put(jsonObject);

            for (ShopLocation mShopLocation : mShopLocations) {
                JSONObject object = new JSONObject();
                object.put("location_name", mShopLocation.getShopLocationName());
                object.put("full_address", mShopLocation.getShopLocationFullAddress());
                object.put("phone_no", mShopLocation.getShopLocationPhone());
                object.put("latitude", mShopLocation.getLatitude());
                object.put("longitude", mShopLocation.getLongitude());
                jsonArray.put(object);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    private void addLocationView(ShopLocation shopLocation) {
        // EditText
        // Instantiate EditText view which will be held inside of
        // TextInputLayout

        TextView location = new TextView(this);
        location.setText(shopLocation.getShopLocationName());
        location.setTextSize(18);
        location.setTextColor(getResources().getColor(android.R.color.black));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        location.setLayoutParams(layoutParams);
        mLLDynamicViews.addView(location);

        TextView fullAddress = new TextView(this);
        fullAddress.setText(shopLocation.getShopLocationFullAddress());
        fullAddress.setTextSize(16);
        fullAddress.setTextColor(getResources().getColor(android.R.color.black));
        fullAddress.setLayoutParams(layoutParams);
        mLLDynamicViews.addView(fullAddress);

        TextView phoneNo = new TextView(this);
        phoneNo.setText(shopLocation.getShopLocationPhone());
        phoneNo.setTextSize(16);
        phoneNo.setTextColor(getResources().getColor(android.R.color.black));
        phoneNo.setLayoutParams(layoutParams);
        mLLDynamicViews.addView(phoneNo);

        View lineView = new View(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
        params.setMargins(0, 5, 0, 5);
        lineView.setLayoutParams(params);
        lineView.setBackgroundColor(getResources().getColor(R.color.line_color));
        mLLDynamicViews.addView(lineView);

       /* // TextInputLayout
        TextInputLayo.adut textInputLayout = new TextInputLayout(this);
//        textInputLayout.setId(View.generateViewId());
        RelativeLayout.LayoutParams textInputLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textInputLayout.setLayoutParams(textInputLayoutParams);*/


    }


    @Override
    public void getResult(int reqCode, Object object) {
        dismissProgress();
        JSONObject jsonObject = (JSONObject) object;
        try {
            if (jsonObject.getInt("code") == 200) {
                showToast(jsonObject.getString("message"));
                getPrefManager().setShopWebsite(mWebsite.getText().toString());
                startActivity(new Intent(this, PostOfferActivity.class));

            } else {
                showAlert(jsonObject.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getErrorResult(Object errorResp) {
        dismissProgress();
        if (errorResp != null) {
            showAlert((String) errorResp);
        }
    }
}
