package com.grabdeals.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.grabdeals.shop.R;
import com.grabdeals.shop.model.ShopLocation;
import com.grabdeals.shop.ui.AddEditMoreLocationsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kondal on 12/27/2016.
 */

public class ShopAddressesAdapter extends ArrayAdapter<ShopLocation> {

    private List<ShopLocation> mShopLocations;
    private Context mContext;
    // View lookup cache
    private static class ViewHolder {
        TextView locName;
        TextView locAddress;
        TextView locPhone;
        ImageView ivEdit;
    }

    public ShopAddressesAdapter(Context context, ArrayList<ShopLocation> users) {
        super(context, R.layout.shop_addresses_list_row, users);
        this.mContext = context;
        this.mShopLocations = mShopLocations;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        ShopLocation location = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.shop_addresses_list_row, parent, false);
            viewHolder.locName = (TextView) convertView.findViewById(R.id.tv_locName);
            viewHolder.locAddress = (TextView) convertView.findViewById(R.id.tv_fullAddress);
            viewHolder.locPhone = (TextView) convertView.findViewById(R.id.tv_phone);
            viewHolder.locPhone = (TextView) convertView.findViewById(R.id.tv_phone);
            viewHolder.ivEdit = (ImageView) convertView.findViewById(R.id.iv_edit);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivEdit.setTag(location);
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.locName.setText(location.getShopLocationName());
        viewHolder.locAddress.setText(location.getShopLocationFullAddress());
        viewHolder.locPhone.setText(location.getShopLocationPhone());

        viewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopLocation locTag = (ShopLocation) v.getTag();
                Intent intent = new Intent(mContext, AddEditMoreLocationsActivity.class);
                intent.putExtra("ShopLocationObj",locTag);
                mContext.startActivity(intent);

            }
        });
        // Return the completed view to render on screen
        return convertView;
    }

}
