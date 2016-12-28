package com.grabdeals.shop.ui;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.toolbox.ImageLoader;
import com.grabdeals.shop.R;
import com.grabdeals.shop.adapter.ShopAddressesAdapter;
import com.grabdeals.shop.model.ShopLocation;
import com.grabdeals.shop.util.APIParams;
import com.grabdeals.shop.util.Constants;
import com.grabdeals.shop.util.FileUtils;
import com.grabdeals.shop.util.ImageUtils;
import com.grabdeals.shop.util.ListUtils;
import com.grabdeals.shop.util.NetworkImageViewRounded;
import com.grabdeals.shop.util.NetworkManager;
import com.grabdeals.shop.util.NetworkUtil;
import com.grabdeals.shop.util.VolleyCallbackListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditShopDetailsActivity extends BaseAppCompatActivity implements View.OnClickListener,VolleyCallbackListener {

    private static final String TAG = "EditShopDetailsActivity";
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
   private ListView mListViewLocations;
    private Button mBtnAddMoreLoc;
    private Button mBtnSaveDetails;

    private int mShopCategoryPos;
    private String mShopCategory;
    private Uri mImageCaptureUri;
    private Bitmap mShopImageBitmap;

    private String mShopID;

    double mLatitude, mLongitude;
    private ShopAddressesAdapter mAdapter;

    private ArrayList<ShopLocation> mShopLocations = new ArrayList<ShopLocation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shop_details);
        findViews();
        if(NetworkUtil.isNetworkAvailable(this)){
            showAlert("Please wait..");
            NetworkManager.getInstance().getRequest(Constants.API_SHOP_GET_PROFILE+getPrefManager().getAccID(),null,this,Constants.API_SHOP_GET_PROFILE_REQ_CODE);
        }else {
            showAlert("Please check your network connection..");
        }
    }

    private void populateData() {

    }

    private void findViews() {
        mImage = (NetworkImageViewRounded) findViewById(R.id.image);
        mIvCamera = (ImageView) findViewById(R.id.iv_camera);
        mAboutShop = (EditText) findViewById(R.id.about_shop);
        mSpinnerCategory = (Spinner) findViewById(R.id.spinner_category);
        mWebsite = (EditText) findViewById(R.id.website);
        mListViewLocations = (ListView) findViewById(R.id.lv_addresses);
      /*  mLocation = (EditText) findViewById(R.id.location);
        mFullAddress = (EditText) findViewById(R.id.full_address);
        mPhoneNumber = (EditText) findViewById(R.id.phone_number);*/
        mBtnAddMoreLoc = (Button) findViewById(R.id.btn_add_more_loc);
        mBtnSaveDetails = (Button) findViewById(R.id.btn_save_details);

        mBtnAddMoreLoc.setOnClickListener(this);
        mBtnSaveDetails.setOnClickListener(this);
        mIvCamera.setOnClickListener(this);
        String imageUrl = Constants.SHOP_AVATAR_URL + getPrefManager().getAccID() + "_" + getPrefManager().getShopID() + ".png";
        if (Constants.DEBUG) Log.d(TAG, "Shop image url" + imageUrl);
        ImageLoader mImageLoader = NetworkManager.getInstance(this)
                .getImageLoader();
        mImage.setDefaultImageResId(R.drawable.default_user);
        mImage.setImageUrl(imageUrl, mImageLoader);
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
                    showProgress("Please wait, Updating Shop Details...");
                    NetworkManager.getInstance().postRequest(Constants.API_ADD_SHOP, preparePostParams(), this, 0);
                } else {
                    showAlert("Please check your network connection..");
                }
            }

        } else if (v == mIvCamera) {
            selectImage();
        }
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

            // Indicates that the activity closed before a selection was made. For example if

            case REQUEST_ADD_MORE_LOCATIONS:
                ShopLocation shopLocation = (ShopLocation) data.getSerializableExtra("LocationObj");
                mShopLocations.add(shopLocation);
                ShopAddressesAdapter adapter = new ShopAddressesAdapter(this,mShopLocations);
                mListViewLocations.setAdapter(adapter);
                ListUtils.setListViewHeightBasedOnItems(mListViewLocations);
//                mAdapter.notifyDataSetChanged();
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
          /*  JSONObject jsonObject = new JSONObject();
            jsonObject.put("location_name", mLocation.getText().toString());
            jsonObject.put("full_address", mFullAddress.getText().toString());
            jsonObject.put("phone_no", mPhoneNumber.getText().toString());
            jsonObject.put("latitude", mLatitude);
            jsonObject.put("longitude", mLongitude);
            jsonArray.put(jsonObject);*/

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
        }else if(hasText(mWebsite)){
            focusView = mWebsite;
            cancel = true;
        } /*else if (hasText(mLocation)) {
            focusView = mLocation;
            cancel = true;
        } else if (hasText(mFullAddress)) {
            focusView = mFullAddress;
            cancel = true;
        } else if (hasText(mPhoneNumber)) {
            focusView = mPhoneNumber;
            cancel = true;
        }*/
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

    @Override
    public void getResult(int reqCode, Object object) {
        dismissProgress();
        JSONObject jsonObject = (JSONObject) object;

    }

    @Override
    public void getErrorResult(Object object) {
        dismissProgress();
        showAlert((String) object);

    }
}
