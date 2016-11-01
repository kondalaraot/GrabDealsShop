package com.grabdeals.shop.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.grabdeals.shop.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PostOfferActivity extends BaseAppCompatActivity implements View.OnClickListener{

    private EditText mOfferTitle;
    private Spinner mSpinnerCategory;
    private EditText mFromDate;
    private EditText mToDate;
    private EditText mLocations;
    private EditText mOfferDescr;
    private EditText mUploadOfferPics;
    private Button mBtnPostOffer;

    private DatePickerDialog mFromDatePickerDialog;
    private DatePickerDialog mToDatePickerDialog;
    private SimpleDateFormat dateFormatter;

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
        mLocations = (EditText)findViewById( R.id.locations );
        mOfferDescr = (EditText)findViewById( R.id.offer_descr );
        mUploadOfferPics = (EditText)findViewById( R.id.upload_offer_pics );
        mBtnPostOffer = (Button)findViewById( R.id.btn_post_offer );

        mBtnPostOffer.setOnClickListener( this );

        mFromDate.setInputType(InputType.TYPE_NULL);
        mFromDate.requestFocus();

        mToDate.setInputType(InputType.TYPE_NULL);
        setDateTimeField();
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

}
