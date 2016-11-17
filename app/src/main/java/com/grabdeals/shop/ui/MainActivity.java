package com.grabdeals.shop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grabdeals.shop.R;
import com.grabdeals.shop.adapter.OffersAdapter;
import com.grabdeals.shop.model.Offer;
import com.grabdeals.shop.util.ClickListener;
import com.grabdeals.shop.util.Constants;
import com.grabdeals.shop.util.DividerItemDecoration;
import com.grabdeals.shop.util.NetworkManager;
import com.grabdeals.shop.util.NetworkUtil;
import com.grabdeals.shop.util.RecyclerTouchListener;
import com.grabdeals.shop.util.VolleyCallbackListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import static android.view.View.VISIBLE;

public class MainActivity extends BaseAppCompatActivity implements VolleyCallbackListener{

    public static final String TAG = "MainActivity";
    RecyclerView mRecyclerView;
    TextView mTextViewEmpty;
    private List<Offer> mOffersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.lv_offers);
        mTextViewEmpty = (TextView) findViewById(R.id.tv_empty);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(MainActivity.this,PostOfferActivity.class);
                startActivity(intent);

            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Offer offer = mOffersList.get(position);
                Intent intent = new Intent(MainActivity.this,OfferDetailsActivity.class);
                intent.putExtra("OFFER_ID",offer.getOfferId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        if(NetworkUtil.isNetworkAvailable(this)){
            showProgress("Please wait, fetching offers...");
            getOffersVolley();
//            getPatientsVolleyStringReq();
        }else{
            showAlert("Please check your network connection..");
        }
    }

    private void getOffersVolley() {
        NetworkManager.getInstance().getRequest(Constants.API_OFFER_ALL,null,this);

    }
    @Override
    public void getResult(Object object) {
        dismissProgress();
        try {
            JSONObject response = (JSONObject) object;

            if (response!=null && response.getInt("code") == 200) {
                JSONArray patientsArray = response.getJSONArray("Content");
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Offer>>(){}.getType();
                mOffersList = gson.fromJson(patientsArray.toString(), listType);
                if(mOffersList.size() >0){
                    OffersAdapter adapter = new OffersAdapter(mOffersList);
                    mRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));
                    LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    mRecyclerView.setAdapter(adapter);
                }else{
                    mTextViewEmpty.setVisibility(VISIBLE);
                }
            } else {
                showAlert(response.getString("message"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
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
