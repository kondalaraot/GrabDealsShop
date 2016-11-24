package com.grabdeals.shop.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grabdeals.shop.R;
import com.grabdeals.shop.model.Offer;

import java.util.List;

/**
 * Created by KTirumalsetty on 11/17/2016.
 */

public class OffersAdapter  extends RecyclerView.Adapter<OffersAdapter.MyViewHolder> {

    private List<Offer> mOffers;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvOfferTitle;
        private TextView mTvOfferDesc;
        private TextView mLabelOfferEnds;
        private TextView mValueOfferEnds;
        private TextView mTvLocation;

        public MyViewHolder(View view) {
            super(view);
//            view.setOnClickListener(this);
            mTvOfferTitle = (TextView)view.findViewById( R.id.tv_offer_title );
            mTvOfferDesc = (TextView)view.findViewById( R.id.tv_offer_desc );
            mLabelOfferEnds = (TextView)view.findViewById( R.id.label_offer_ends );
            mValueOfferEnds = (TextView)view.findViewById( R.id.value_offer_ends );
            mTvLocation = (TextView)view.findViewById( R.id.tv_location );
        }

    }

    public OffersAdapter(List<Offer> patientsList) {
        this.mOffers = patientsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.offer_list_row, viewGroup, false);
        MyViewHolder itemViewHolder = new MyViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Offer offer = mOffers.get(position);
        holder.mTvOfferTitle.setText(offer.getTitle());
        holder.mTvOfferDesc.setText(offer.getDescription());
        holder.mValueOfferEnds.setText(offer.getOffer_end());
        if(offer.getLocations()!=null && offer.getLocations().size()>0){
            holder.mTvLocation.setText(offer.getLocations().get(0).getArea_name());
        }else{
            holder.mTvLocation.setText("No location");

        }

    }

    @Override
    public int getItemCount() {
        return mOffers.size();
    }
}
