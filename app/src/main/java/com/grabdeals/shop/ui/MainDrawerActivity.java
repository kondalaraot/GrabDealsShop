package com.grabdeals.shop.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import com.grabdeals.shop.util.NetworkImageViewRounded;
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

public class MainDrawerActivity extends BaseAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,VolleyCallbackListener {

    public static final String TAG = "MainDrawerActivity";

    private RecyclerView mRecyclerView;
    private TextView mTextViewEmpty;
    private List<Offer> mOffersList;
    private OffersAdapter mOffersAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    TextView mShopName;
    TextView mShopMobileNo;
    NetworkImageViewRounded mImageViewRounded;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setLogo(R.mipmap.ic_launcher);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
         mShopName= (TextView) header.findViewById(R.id.tv_shop_name);
        mShopMobileNo = (TextView) header.findViewById(R.id.tv_mobile);
        mImageViewRounded = (NetworkImageViewRounded) header.findViewById(R.id.imageView);

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
                Intent intent = new Intent(MainDrawerActivity.this, PostOfferActivity.class);
                startActivity(intent);

            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Offer offer = mOffersList.get(position);
                Intent intent = new Intent(MainDrawerActivity.this, OfferDetailsActivity.class);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        populateShopDetails();
    }

    private void populateShopDetails(){
        mShopName.setText(getPrefManager().getShopName());
        mShopMobileNo.setText(getPrefManager().getShopMoBileNo());
        mImageViewRounded.setDefaultImageResId(R.drawable.default_user);
        mImageViewRounded.setImageUrl(getPrefManager().getShopUrl(),NetworkManager.getInstance().getImageLoader());
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
                    mOffersAdapter = new OffersAdapter(mOffersList);
                    mRecyclerView.addItemDecoration(new DividerItemDecoration(MainDrawerActivity.this, LinearLayoutManager.VERTICAL));
                    LinearLayoutManager llm = new LinearLayoutManager(MainDrawerActivity.this);
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

   /* @Override
    public void onBackPressed() {
//        super.onBackPressed();
        showLogoutAlert();
    }*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            showLogoutAlert();
        }
    }

    private void getOffersVolley() {
        NetworkManager.getInstance().getRequest(Constants.API_OFFER_ALL, null, this,0);

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            // Handle the camera action
            startActivity(new Intent(this,EditShopDetailsActivity.class));
        } else if (id == R.id.nav_post_offer) {
            startActivity(new Intent(this,PostOfferActivity.class));
        } else if (id == R.id.nav_share) {
            Intent i=new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT,"GrabDeals Shop");
            i.putExtra(android.content.Intent.EXTRA_TEXT, "post your offers through this app");
            startActivity(Intent.createChooser(i,"Share via"));
        } else if (id == R.id.nav_sign_out) {
                showLogoutAlert();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
/*
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }*/
}
