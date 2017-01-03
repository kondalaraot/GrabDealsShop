package com.grabdeals.shop.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.grabdeals.shop.R;
import com.grabdeals.shop.adapter.CountriesAdapter;
import com.grabdeals.shop.model.Country;
import com.grabdeals.shop.util.APIParams;
import com.grabdeals.shop.util.Constants;
import com.grabdeals.shop.util.FileUtils;
import com.grabdeals.shop.util.ImageUtils;
import com.grabdeals.shop.util.NetworkManager;
import com.grabdeals.shop.util.NetworkUtil;
import com.grabdeals.shop.util.VolleyCallbackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterShopKeeperActivity extends BaseAppCompatActivity implements View.OnClickListener,VolleyCallbackListener{

    private final String TAG = "RegisterShopKeeperAct";

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;

    private static final int TAKE_PICTURE = 100;
    private static final int REQUEST_CAMERA = 101;
    private static final int SELECT_FILE = 102;


    private ScrollView mCreateNewAcForm;
    private LinearLayout mLlParentCreateAcc;
    private ImageView mImage;
    private ImageView mImageCamera;
    private Spinner mSpinnerCountries;
    private EditText mPhoneNo;
    private EditText mShopName;
    private EditText mPassword;
    private Button mBtnCreateAcc;
    private Button mBtnLogin;

    Uri mImageCaptureUri;
    Bitmap mShopImageBitmap;
    Bitmap mShopImageCircleBitmap;

    List<Country> mCountries;

    final CharSequence[] items = { "Take Photo", "Choose from Library",
            "Cancel" };

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-10-29 18:26:58 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shop_keeper);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        findViews();


    }

    private void findViews() {
        mCreateNewAcForm = (ScrollView)findViewById( R.id.create_new_ac_form );
        mLlParentCreateAcc = (LinearLayout)findViewById( R.id.ll_parent_create_acc );
        mImage = (ImageView) findViewById( R.id.image_shop );
        mImageCamera = (ImageView) findViewById( R.id.iv_camera );

        mSpinnerCountries = (Spinner) findViewById( R.id.spinner_countries );
        mPhoneNo = (EditText)findViewById( R.id.phone_no );
        mShopName = (EditText)findViewById( R.id.shop_name );
        mPassword = (EditText)findViewById( R.id.password );
        mBtnCreateAcc = (Button)findViewById( R.id.btn_create_acc );
        mBtnLogin = (Button)findViewById( R.id.btn_login );

        mBtnCreateAcc.setOnClickListener( this );
        mBtnLogin.setOnClickListener( this );
        mImageCamera.setOnClickListener( this );

        mCountries = FileUtils.loadJSONFromAsset(this);
//        ArrayAdapter<Country> myAdapter = new ArrayAdapter<Country>(this, android.R.layout.simple_spinner_item, mCountries);
        String locale = this.getResources().getConfiguration().locale.getCountry();
        Log.d(TAG,"locale");

        CountriesAdapter adapter = new CountriesAdapter(this,android.R.layout.simple_spinner_item,mCountries);
        mSpinnerCountries.setAdapter(adapter);


//        mImage.setDefaultImageResIdsetDefaultImageResId(R.drawable.office_building_icon);

    }
    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("counties.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    /**
     * Handle button click events<br />
     *
     */
    @Override
    public void onClick(View v) {
        if ( v == mBtnCreateAcc ) {
            // Handle clicks for mBtnCreateAcc
            if (validate())
                if(NetworkUtil.isNetworkAvailable(this)){
                    showProgress("Please wait...");
                    NetworkManager.getInstance().postRequest(Constants.API_IS_REGISTER,preparePostParams(),this,0);
                }else{
                    showAlert("Please check your network connection..");
                }
        } else if ( v == mBtnLogin ) {
            // Handle clicks for mBtnLogin
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }else if (v == mImageCamera){
            selectImage();
        }
    }

    private boolean validate() {
        boolean isValid = true;
        boolean cancel = false;
        View focusView = null;

        // Check for a valid mobile number.
        if(hasText(mPhoneNo)){
            focusView = mPhoneNo;
            cancel = true;
        }else if(hasText(mShopName)){
            focusView = mShopName;
            cancel = true;
        }else if(hasText(mPassword)){
            focusView = mPassword;
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
        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put(APIParams.PARAM_MOBILE_NO, mPhoneNo.getText().toString());
        jsonParams.put(APIParams.PARAM_SHOP_NAME, mShopName.getText().toString());
        return jsonParams;
    }


    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
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
                   /* Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image*//*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);*/
                    // pick from file
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    //intent.setType("image/*");
                    //intent.setAction(Intent.);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"),PICK_FROM_FILE);
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
                    mShopImageCircleBitmap = ImageUtils.getCircularBitmapWithWhiteBorder(this,mShopImageBitmap, 8);
                    mImage.setImageBitmap(mShopImageCircleBitmap);
                }

                // File f = new File(mImageCaptureUri.getPath());
                //
                // if (f.exists())
                // f.delete();

                break;
            /*case PICK_CONTACT:
                if (resultCode == RESULT_OK) {
                    uriContact = data.getData();
                    retrieveContactNumber();
                }*/
        }
    }

    private void doCrop() {

        Log.d(TAG, "mImageCaptureUri---- "+mImageCaptureUri);
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

    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void getResult(int reqCode,Object object) {
        dismissProgress();
        if (object != null) {
            JSONObject jsonObject = (JSONObject) object;

            try {
                if(jsonObject.getInt("code") == 200){
                    Intent intent = new Intent(this,ConfirmOTPActivity.class);
                    intent.putExtra("KEY_MOBILE_NO",mPhoneNo.getText().toString());
                    intent.putExtra("KEY_PASSWORD",mPassword.getText().toString());
                    intent.putExtra("KEY_SHOP_NAME",mShopName.getText().toString());
                    intent.putExtra("KEY_IMAGE_BITMAP",mShopImageBitmap);
                    intent.putExtra("KEY_IMAGE_URI",mImageCaptureUri);

                    startActivity(intent);
                    finish();
                }else{
                    showAlert(jsonObject.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


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
