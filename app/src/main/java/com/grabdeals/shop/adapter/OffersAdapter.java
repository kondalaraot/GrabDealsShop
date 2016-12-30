package com.grabdeals.shop.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.grabdeals.shop.R;
import com.grabdeals.shop.model.Location;
import com.grabdeals.shop.model.Offer;
import com.grabdeals.shop.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by KTirumalsetty on 11/17/2016.
 */

public class OffersAdapter  extends RecyclerView.Adapter<OffersAdapter.MyViewHolder> implements Filterable{

    private List<Offer> mOffers;
    private List<Offer> mOriginalOffers;
    private String mSearchText;
    private Context mContext;

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

    public OffersAdapter(List<Offer> offerList, Context context) {
        this.mContext = context;
        this.mOffers = offerList;
        this.mOriginalOffers = offerList;
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
        String offerTitleFullText = offer.getTitle();
//        holder.mTvOfferTitle.setText(offer.getTitle());
        holder.mTvOfferDesc.setText(offer.getDescription());
        holder.mValueOfferEnds.setText(offer.getOffer_end());

        if(offer.getLocations()!=null && offer.getLocations().size()>0){
           StringBuilder builder = new StringBuilder();
            for (Location location : offer.getLocations()) {
                builder.append(location.getArea_name()+",");
            }
            String locations = builder.toString();
            holder.mTvLocation.setText(locations.substring(0,locations.length()-1));
        }else{
            holder.mTvLocation.setText("No location");

        }

        // highlight search text
        if (mSearchText != null && !mSearchText.isEmpty()) {
            int startPos = offerTitleFullText.toLowerCase(Locale.US).indexOf(mSearchText.toLowerCase(Locale.US));
            int endPos = startPos + mSearchText.length();

            if (startPos != -1) {
                Spannable spannable = new SpannableString(offerTitleFullText);

                ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{mContext.getResources().getColor(R.color.colorAccent),});
                TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.mTvOfferTitle.setText(spannable);
            } else {
                holder.mTvOfferTitle.setText(offerTitleFullText);
            }
        } else {
            holder.mTvOfferTitle.setText(offerTitleFullText);
        }

    }

    @Override
    public int getItemCount() {
        return mOffers.size();
    }

    @Override
    public Filter getFilter() {
        return new OffersFilter();
    }
    public Offer getItem(int pos) {
        return mOffers.get(pos);
    }


    public class OffersFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            mSearchText = constraint.toString();
            if(Constants.DEBUG) Log.d(TAG,"performFiltering ");
            FilterResults filterResults = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                filterResults.values = mOriginalOffers;
                filterResults.count = mOriginalOffers.size();
            }
            else {
                List<Offer> filteredList= new ArrayList<Offer>();
                for (int i = 0; i < mOriginalOffers.size(); i++) {
                    Offer offer = mOriginalOffers.get(i);
                    if (offer.getTitle().toUpperCase().contains(constraint.toString().toUpperCase()))  {
                        filteredList.add(offer);
                    }
                }
                filterResults.values = filteredList;
                filterResults.count = filteredList.size();
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            if(Constants.DEBUG) Log.d(TAG,"publishResults ");

			/*if (results.count == 0) {
	            notifyDataSetInvalidated();
	        } else {*/
            mOffers =  (ArrayList<Offer>) results.values;
            notifyDataSetChanged();
            //AddGroup.setListViewHeightBasedOnChildren(mListView);

            //}
        }


    }
    
}
