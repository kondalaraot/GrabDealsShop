package com.grabdeals.shop.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends BaseAppCompatActivity implements VolleyCallbackListener {

    public static final String TAG = "MainActivity";
    RecyclerView mRecyclerView;
    TextView mTextViewEmpty;
    private List<Offer> mOffersList;
    OffersAdapter mOffersAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

        mRecyclerView = (RecyclerView) findViewById(R.id.lv_offers);
        mTextViewEmpty = (TextView) findViewById(R.id.tv_empty);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        getOffersVolley();
                    }
                }, 2000);
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(MainActivity.this, PostOfferActivity.class);
                startActivity(intent);

            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Offer offer = mOffersAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, OfferDetailsActivity.class);
                intent.putExtra("OFFER_ID", offer.getOffer_id());
                intent.putExtra("OFFER_OBJ", offer);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        if (NetworkUtil.isNetworkAvailable(this)) {
            showProgress("Please wait, fetching offers...");
            getOffersVolley();
//            getPatientsVolleyStringReq();
        } else {
            showAlert("Please check your network connection..");
        }
    }

    private void getOffersVolley() {
        NetworkManager.getInstance().getRequest(Constants.API_OFFER_ALL, null, this,0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        inflater.inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate( R.menu.menu_home, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) item.getActionView();
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                    mOffersAdapter.getFilter().filter(newText);

                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
// Do something when collapsed
//                        adapter.setFilter(mCountryModel);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
// Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            showLogoutAlert();

        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getPrefManager().logoutUser();
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void getResult(int reqCode,Object object) {
        dismissProgress();
        try {
            JSONObject response = (JSONObject) object;

            if (response!=null && response.getInt("code") == 200) {
                JSONArray data = response.getJSONArray("data");
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Offer>>(){}.getType();
                mOffersList = gson.fromJson(data.toString(), listType);
                if(mOffersList.size() >0){
                    mOffersAdapter = new OffersAdapter(mOffersList,this);
                    mRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));
                    LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    mRecyclerView.setAdapter(mOffersAdapter);
                }else{
                    mTextViewEmpty.setVisibility(VISIBLE);
                }
            } else {
                showAlert(response.getString("message"));
                mTextViewEmpty.setVisibility(VISIBLE);


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

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        showLogoutAlert();
    }
}
