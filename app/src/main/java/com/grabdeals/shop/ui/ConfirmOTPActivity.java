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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.grabdeals.shop.R;
import com.grabdeals.shop.util.APIParams;
import com.grabdeals.shop.util.Constants;
import com.grabdeals.shop.util.FileUtils;
import com.grabdeals.shop.util.ImageUtils;
import com.grabdeals.shop.util.NetworkManager;
import com.grabdeals.shop.util.VolleyCallbackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfirmOTPActivity extends BaseAppCompatActivity implements View.OnClickListener,VolleyCallbackListener{

    private static final String TAG = "ConfirmOTPActivity";

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;

    private ImageView mImage;
    private ImageView mSelectImage;
    private TextView mOtpSentTo;
    private EditText mEnterOtp;
    private Button mBtnVerify;
    private Button mBtnResendOtp;

    private String mobileNo;
    private String shopName;
    private String password;
    private Uri mImageCaptureUri;
    private Bitmap mShopImageBitmap;
    private Bitmap mShopImageCircledBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_otp);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        mobileNo = getIntent().getStringExtra("KEY_MOBILE_NO");
        password = getIntent().getStringExtra("KEY_PASSWORD");
        shopName = getIntent().getStringExtra("KEY_SHOP_NAME");
        mShopImageBitmap = getIntent().getParcelableExtra("KEY_IMAGE_BITMAP");
        mImageCaptureUri = getIntent().getParcelableExtra("KEY_IMAGE_URI");
        findViews();
        if(mShopImageBitmap !=null){
            Bitmap mShopImageCircleBitmap = ImageUtils.getCircularBitmapWithWhiteBorder(this,mShopImageBitmap, 8);
            mImage.setImageBitmap(mShopImageCircleBitmap);


        }

    }
    /**
     * Find the Views in the layout<br />
     *
     */
    private void findViews() {
        mImage = (ImageView)findViewById( R.id.image_shop );
        mSelectImage = (ImageView)findViewById( R.id.iv_camera );
        mOtpSentTo = (TextView)findViewById( R.id.otp_sent_to );
        mEnterOtp = (EditText)findViewById( R.id.enter_otp );
        mBtnVerify = (Button)findViewById( R.id.btn_verify );
        mBtnResendOtp = (Button)findViewById( R.id.btn_resend_otp );
        mOtpSentTo.append(mobileNo);
        mBtnVerify.setOnClickListener( this );
        mBtnResendOtp.setOnClickListener( this );
        mSelectImage.setOnClickListener( this );

    }

    /**
     * Handle button click events<br />
     */
    @Override
    public void onClick(View v) {
        if ( v == mBtnVerify ) {
            // Handle clicks for mBtnVerify
            if(validate()){
                showProgress("signing up...");
                NetworkManager.getInstance().postRequest(Constants.API_SIGN_UP,preparePostParams(),this,Constants.API_SIGN_UP_REQ_CODE);

            }
        } else if ( v == mBtnResendOtp ) {
            // Handle clicks for mBtnResendOtp
            showProgress("Sending OTP...");
            NetworkManager.getInstance().postRequest(Constants.API_SEND_OTP_SIGN_UP,prepareSendOtpPostParams(),this,Constants.API_SEND_OTP_SIGN_UP_REQ_CODE);
        }else if ( v == mSelectImage ) {
            // Handle clicks for mBtnResendOtp
            selectImage();
        }
    }

    private boolean validate() {
         boolean isValid = true;
        boolean cancel = false;
        View focusView = null;
        // Store values at the time of the login attempt.
        String otp = mEnterOtp.getText().toString();
//        String password = mPasswordView.getText().toString();
        // Check for a valid mobile number.
        if (TextUtils.isEmpty(otp)) {
            mEnterOtp.setError(getString(R.string.error_field_required));
            focusView = mEnterOtp;
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

    private Map<String,String> preparePostParams(){
        Map<String, String> formParams = new HashMap<>();
        formParams.put(APIParams.PARAM_MOBILE_NO, mobileNo);
        formParams.put(APIParams.PARAM_SHOP_NAME, shopName);
        formParams.put(APIParams.PARAM_PASSWORD, password);
        formParams.put(APIParams.PARAM_OTP_CODE, mEnterOtp.getText().toString());
        if(mShopImageBitmap != null)
        formParams.put(APIParams.PARAM_FILE_DATA, FileUtils.convertBitmapToBase64(mShopImageBitmap));
        return formParams;
    }

    private Map<String,String> prepareSendOtpPostParams(){
        Map<String, String> formParams = new HashMap<>();
        formParams.put(APIParams.PARAM_MOBILE_NO, mobileNo);

        return formParams;
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
                    mShopImageCircledBitmap = ImageUtils.getCircularBitmapWithWhiteBorder(this,mShopImageBitmap, 8);
                    mImage.setImageBitmap(mShopImageCircledBitmap);
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



    @Override
    public void getResult(int reqCode,Object object) {
        dismissProgress();

        if(object!=null){
            JSONObject jsonObject = (JSONObject) object;
            try {
                if(jsonObject.getInt("code") == 200){
//                    showAlert(jsonObject.getString("message"));
                    if(reqCode == Constants.API_SEND_OTP_SIGN_UP_REQ_CODE){
                        showAlert(jsonObject.getString("message"));

                    }else{
                        JSONObject data = jsonObject.getJSONObject("data");
                        String authToken = data.getString("auth_token");
                        JSONObject account = data.getJSONObject("account");
                        getPrefManager().setAuthToken(authToken);
                        getPrefManager().setAccountID(account.getString("acc_id"));
                        getPrefManager().setShopID(account.getString("shop_id"));
                        getPrefManager().setShopName(account.getString("shop_name"));
                        getPrefManager().setShopMobileNO(account.getString("mobile_no"));
                        startActivity(new Intent(this,EnterShopDetailsActivity.class));
                        finish();
                    }
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
