package com.grabdeals.shop.ui;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.models.Image;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grabdeals.shop.R;
import com.grabdeals.shop.model.ShopBranch;
import com.grabdeals.shop.util.APIParams;
import com.grabdeals.shop.util.Constants;
import com.grabdeals.shop.util.FileUtils;
import com.grabdeals.shop.util.MultiSelectionSpinner;
import com.grabdeals.shop.util.NetworkManager;
import com.grabdeals.shop.util.NetworkUtil;
import com.grabdeals.shop.util.VolleyCallbackListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PostOfferActivity extends BaseAppCompatActivity implements View.OnClickListener,VolleyCallbackListener{

    private static final String TAG = "PostOfferActivity" ;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final int PICK_FROM_CAMERA = 10;
    private static final int CROP_FROM_CAMERA = 20;
    private static final int PICK_FROM_FILE = 30;

    private EditText mOfferTitle;
    private Spinner mSpinnerCategory;
    private EditText mFromDate;
    private EditText mToDate;
//    private EditText mLocations;
    private MultiSelectionSpinner mLocations;
    private EditText mOfferDescr;
    private EditText mUploadOfferPics;
    private Button mBtnPostOffer;

    private DatePickerDialog mFromDatePickerDialog;
    private DatePickerDialog mToDatePickerDialog;
    private SimpleDateFormat dateFormatter =  new SimpleDateFormat("dd-MM-yyyy", Locale.US);;

    private int mOfferCategoryPos;
    private String mOffereCategory;
    private Uri mImageCaptureUri;
    Bitmap mAttachmentBitmap;
    List<ShopBranch> shopBranches;
    ArrayList<Image> mImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_offer);
        findViews();
    }

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-11-01 17:40:24 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        mOfferTitle = (EditText)findViewById( R.id.offer_title );
        mSpinnerCategory = (Spinner)findViewById( R.id.spinner_category );
        mFromDate = (EditText)findViewById( R.id.from_date );
        mToDate = (EditText)findViewById( R.id.to_date );
        mLocations = (MultiSelectionSpinner) findViewById( R.id.spinnerLocations );

        mOfferDescr = (EditText)findViewById( R.id.offer_descr );
        mUploadOfferPics = (EditText)findViewById( R.id.upload_offer_pics );
        mBtnPostOffer = (Button)findViewById( R.id.btn_post_offer );

        mBtnPostOffer.setOnClickListener( this );

        mFromDate.setInputType(InputType.TYPE_NULL);
        mFromDate.requestFocus();

        mToDate.setInputType(InputType.TYPE_NULL);
        setDateTimeField();

//        mLocations.setInputType(InputType.TYPE_NULL);
        mUploadOfferPics.setInputType(InputType.TYPE_NULL);

        mUploadOfferPics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


       /* mLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity();
            }
        });*/

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
                mOffereCategory = adapterView.getItemAtPosition(pos).toString();
                mOfferCategoryPos = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


       /* if(MyApplication.sAccount == null ){*/
            if(NetworkUtil.isNetworkAvailable(this)){
                showProgress("Getting shop Locations..");
                NetworkManager.getInstance().getRequest(Constants.API_SHOP_LOCATIONS+getPrefManager().getShopID(),null,this,Constants.API_SHOP_LOCATIONS_REQ_CODE);
            }else{
                showAlert("Please check your network ");
            }

        /*}else{
            shopBranches =  MyApplication.sAccount.getShop_branches();
            populateLocationsSpinner(shopBranches);
        }*/


        mLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void populateLocationsSpinner(List<ShopBranch> shopBranches){
        List<String> locations = new ArrayList<String>();
//        locations.add("--Select locations--");
        for (ShopBranch shopBranch : shopBranches) {
            locations.add(shopBranch.getLocation_name());
        }
        mLocations.setItems(locations);

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
                   /* Intent intent = new Intent(Intent.ACTION_PICK);
                    //intent.setType("image*//*");
                    //intent.setAction(Intent.);
                    intent.setType("image*//*");
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"),PICK_FROM_FILE);*/

                    Intent intent = new Intent(PostOfferActivity.this, AlbumSelectActivity.class);
//set limit on number of images that can be selected, default is 10
                    intent.putExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_LIMIT, 10);
                    startActivityForResult(intent, PICK_FROM_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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
     * Handle button click events<br />
     * <br />
     * Auto-created on 2016-11-01 17:40:24 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == mBtnPostOffer ) {
            // Handle clicks for mBtnSaveDetails
            if (validate()){
                if(NetworkUtil.isNetworkAvailable(this)){
                    showProgress("Posting offer...");
                    NetworkManager.getInstance().postRequest(Constants.API_POST_OFFER,preparePostParams(),this,Constants.API_POST_OFFER_REQ_CODE);
                }else{
                    showAlert("Please check your network connection..");
                }
            }
        }if(v == mFromDate) {
            mFromDatePickerDialog.show();
        } else if(v == mToDate) {
            mToDatePickerDialog.show();
        }
    }

    private void setDateTimeField() {
        mFromDate.setOnClickListener(this);
        mToDate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        mFromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mFromDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        mToDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mToDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
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
               /* mImageCaptureUri = data.getData();
                doCrop();*/
                //The array list has the image paths of the selected images
                 mImages = data.getParcelableArrayListExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_IMAGES);
                StringBuilder builder = new StringBuilder();
                for (Image image : mImages) {
                    String path = image.path;
                    Log.d(TAG, "path " + path);
                    String filename = path.substring(path.lastIndexOf("/") + 1);
                    Log.d(TAG, "filename " + filename);
                    builder.append(filename + "\n");
                }

                mUploadOfferPics.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
                mUploadOfferPics.setText(builder.toString());

                break;

            case CROP_FROM_CAMERA:
                Bundle extras = data.getExtras();

                if (extras != null) {
                    mAttachmentBitmap = extras.getParcelable("data");
//                    mShopImageBitmap = ImageUtils.getCircularBitmapWithWhiteBorder(this,photo, 8);
//                    mImage.setImageBitmap(mShopImageBitmap);
//                    mImage.set(mShopImageBitmap);
//                    mImage.setLocalImageBitmap(mShopImageBitmap);
                    mUploadOfferPics.setText(FileUtils.getFileName(this,mImageCaptureUri));

                }

                // File f = new File(mImageCaptureUri.getPath());
                //
                // if (f.exists())
                // f.delete();

                break;
            case REQUEST_CODE_AUTOCOMPLETE:
               /* // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place Selected: " + place.getName());

                // Format the place's details and display them in the TextView.
                if(TextUtils.isEmpty(mLocations.getText())){
                    mLocations.setText(place.getName());

                }else{
                    mLocations.setText(mLocations.getText().toString()+","+place.getName());

                }
                       *//* mLocation.setText(formatPlaceDetails(getResources(), place.getName(),
                                place.getId(), place.getAddress(), place.getPhoneNumber(),
                                place.getWebsiteUri()).toString());*//*
                LatLng latLng = place.getLatLng();
                double mLatitude = latLng.latitude;
                double mLongitude = latLng.longitude;
                Log.d(TAG,"lat,long are "+mLatitude+"  "+mLongitude);
                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
//                            mLocation.setText(Html.fromHtml(attributions.toString()));
                    Log.d(TAG,"attributions "+attributions.toString());
                } else {
//                            mLocation.setText("");
                    Log.d(TAG,"attributions empty");
                }*/
                break;
            // Indicates that the activity closed before a selection was made. For example if
            // the user pressed the back button.
            case PlaceAutocomplete.RESULT_ERROR:
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
                break;

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

    private boolean validate() {
        boolean isValid = true;
        boolean cancel = false;
        View focusView = null;
        // Store values at the time of the login attempt.

//        String password = mPasswordView.getText().toString();
        // Check for a valid mobile number.
        if(hasText(mOfferTitle)){
            focusView = mOfferTitle;
            cancel = true;
        }else if(hasSpinnerSelected(mSpinnerCategory)){
            focusView = mSpinnerCategory;
            cancel = true;
        }else if(hasText(mFromDate)){
            focusView = mFromDate;
            cancel = true;
        }else if(hasText(mToDate)){
            focusView = mToDate;
            cancel = true;
        }else if(hasLocSpinnerSelected(mLocations)){
            focusView = mLocations;
            cancel = true;
        }else if(hasText(mOfferDescr)){
            focusView = mOfferDescr;
            cancel = true;
        }/*else if(hasText(mUploadOfferPics)){
            focusView = mUploadOfferPics;
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


    protected boolean hasLocSpinnerSelected(MultiSelectionSpinner spinner) {
        if(spinner.getSelectedStrings().size()>0)
            return false;
        else{
            ((TextView)spinner.getSelectedView()).setError("locations is required");
            return true;

        }
    }

    private Map<String,String> preparePostParams(){
        Map<String, String> formParams = new HashMap<>();
        formParams.put(APIParams.PARAM_OFFER_TITLE, mOfferTitle.getText().toString());
        formParams.put(APIParams.PARAM_DESCRIPTION, mOfferDescr.getText().toString());
        formParams.put(APIParams.PARAM_OFR_START, mFromDate.getText().toString());
        formParams.put(APIParams.PARAM_OFR_END, mToDate.getText().toString());
        formParams.put(APIParams.PARAM_LOCATIONS, prepareLocationsInfo());
//        if(mAttachmentBitmap!=null)
//            formParams.put(APIParams.PARAM_ATTACHMENTS, FileUtils.convertBitmapToBase64(mAttachmentBitmap));
        return formParams;
    }

    private String prepareLocationsInfo() {
        String locations;
        StringBuilder builder = new StringBuilder();
        List<Integer> offerSelectedIndices = mLocations.getSelectedIndicies();
        for (Integer offerSelectedIndice : offerSelectedIndices) {
            ShopBranch selectedBranch = shopBranches.get(offerSelectedIndice);
            builder.append(selectedBranch.getShop_area_id()+",");
        }
        locations = builder.toString();
        return locations.substring(0, locations.lastIndexOf(","));
    }


    @Override
    public void getResult(int reqCode,Object object) {
                dismissProgress();
        try  {

            JSONObject jsonObject = (JSONObject) object;
            if(reqCode == Constants.API_SHOP_LOCATIONS_REQ_CODE){
                if(jsonObject.getInt("code") == 200){
                    JSONArray data = jsonObject.getJSONArray("data");
                    Type listType = new TypeToken<List<ShopBranch>>() {}.getType();
                    shopBranches = new Gson().fromJson(data.toString(), listType);
                    populateLocationsSpinner(shopBranches);
                }else{
                    showAlert("No shop locations found..");
                }


            }else if(reqCode == Constants.API_POST_OFFER_REQ_CODE){
                JSONObject data = jsonObject.getJSONObject("data");
                int offerId = data.getInt("offer_id");
                showToast("Offer posted successfully..");
                uploadOfferImages(offerId);
                    /*Intent intent = new Intent(this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
            }else if(reqCode == Constants.API_SHOP_UPLOAD_OFFER_ATTACHMENTS_REQ_CODE){
                if(jsonObject.getInt("code") == 200){
                    showToast("Picture uploaded successfully..");
                }else{
                    showAlert(jsonObject.getString("message"));
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void uploadOfferImages(int offerID) {

        if(NetworkUtil.isNetworkAvailable(this)){

            for (Image mImage : mImages) {
                showProgress("Uploading attachments...");
                HashMap<String,String> map = new HashMap<String,String>();
                map.put(APIParams.PARAM_OFFER_ID,String.valueOf(offerID));
                map.put(APIParams.PARAM_FILE_DATA, FileUtils.convertToBase64(mImage.path));
                NetworkManager.getInstance().postRequest(Constants.API_SHOP_UPLOAD_OFFER_ATTACHMENTS,map,this,Constants.API_SHOP_UPLOAD_OFFER_ATTACHMENTS_REQ_CODE);
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
