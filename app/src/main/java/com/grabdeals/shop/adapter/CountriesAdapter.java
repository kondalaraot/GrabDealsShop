package com.grabdeals.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.grabdeals.shop.model.Country;

import java.util.List;

/**
 * Created by KTirumalsetty on 1/3/2017.
 */

public class CountriesAdapter extends ArrayAdapter<Country> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (Country)
    private List<Country> values;
    LayoutInflater inflater;


    public CountriesAdapter(Context context, int textViewResourceId,
                       List<Country> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount(){
        return values.size();
    }

    public Country getItem(int position){
        return values.get(position);
    }

    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
       /* TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Countrys array) and the current position
        // You can NOW reference each method you has created in your bean object (Country class)
        label.setText(values.get(position).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;*/
        return getCustomView(position, convertView, parent);

    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
       /* TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getName());*/
        return getCustomDropDownView(position, convertView, parent);
//        return label;
    }

    private View getCustomDropDownView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);

        /***** Get each Model object from Arraylist ********/
//        tempValues = null;
        Country country = (Country) values.get(position);

        TextView label        = (TextView)row.findViewById(android.R.id.text1);
      /*  TextView sub          = (TextView)row.findViewById(R.id.sub);
        ImageView companyLogo = (ImageView)row.findViewById(R.id.image);*/
        label.setText(country.getName());

       /* if(position==0){

            // Default selected Spinner item
            label.setText("-Select Country-");
//            sub.setText("");
        }
        else
        {
            // Set values for spinner each row
            label.setText(country.getName());
           *//* sub.setText(tempValues.getUrl());
            companyLogo.setImageResource(res.getIdentifier
                    ("com.androidexample.customspinner:drawable/"
                            + tempValues.getImage(),null,null));*//*

        }*/

        return row;
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);

        /***** Get each Model object from Arraylist ********/
//        tempValues = null;
        Country country = (Country) values.get(position);

        TextView label        = (TextView)row.findViewById(android.R.id.text1);
      /*  TextView sub          = (TextView)row.findViewById(R.id.sub);
        ImageView companyLogo = (ImageView)row.findViewById(R.id.image);*/
        label.setText(country.getCode());

       /* if(position==0){

            // Default selected Spinner item
            label.setText("-Select Country-");
//            sub.setText("");
        }
        else
        {
            // Set values for spinner each row
            label.setText(country.getName());
           *//* sub.setText(tempValues.getUrl());
            companyLogo.setImageResource(res.getIdentifier
                    ("com.androidexample.customspinner:drawable/"
                            + tempValues.getImage(),null,null));*//*

        }*/

        return row;
    }
}