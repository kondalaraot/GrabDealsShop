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
import com.grabdeals.shop.util.NetworkImageViewRounded;

public class EnterShopDetailsActivity extends BaseAppCompatActivity implements View.OnClickListener{


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

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-11-01 12:39:48 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
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


    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2016-11-01 12:39:48 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == mBtnAddMoreLoc ) {
            // Handle clicks for mBtnLogin
        } else if ( v == mBtnSaveDetails ) {
            // Handle clicks for mBtnSaveDetails
        }
    }
}
