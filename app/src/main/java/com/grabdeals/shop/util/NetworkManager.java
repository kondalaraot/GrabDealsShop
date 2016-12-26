package com.grabdeals.shop.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by KTirumalsetty on 10/31/2016.
 */

public class NetworkManager
{
    private static final String TAG = "NetworkManager";

    private static final int MY_SOCKET_TIMEOUT_MS = 5000;
    private static NetworkManager mInstance = null;

    //for Volley API
    // Default maximum disk usage in bytes
    private static final int DEFAULT_DISK_USAGE_BYTES = 25 * 1024 * 1024;
    // Default cache folder name
    private static final String DEFAULT_CACHE_DIR = "photos";
    public RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    LruBitmapCache mLruBitmapCache;

    private Context mContext;


    private NetworkManager(Context context)
    {
        mContext=context;
//        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        mRequestQueue = getRequestQueue();
        //other stuf if you need
        /*mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });*/
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) { }

            @Override
            public Bitmap getBitmap(String key) {
                return null;
            }
        });
    }

    public static void clearCache(RequestQueue requestQueue) {
        requestQueue.getCache().clear();
    }

    public static synchronized NetworkManager getInstance(Context context)
    {
        if (null == mInstance)
            mInstance = new NetworkManager(context);
        return mInstance;
    }

    //this is so you don't need to pass context each time
    public static synchronized NetworkManager getInstance()
    {
        if (null == mInstance)
        {
            throw new IllegalStateException(NetworkManager.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return mInstance;
    }

    /*public ImageLoader getImageLoader() {
        return mImageLoader;
    }*/
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            getLruBitmapCache();
            mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
        }

        return this.mImageLoader;
        //return imageLoader;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(mContext.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            // Don't forget to start the volley request queue
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public LruBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null)
            mLruBitmapCache = new LruBitmapCache();
        return this.mLruBitmapCache;
    }


    public void postRequest(String urlSuffix, Object postParams, final VolleyCallbackListener<Object> listener, final int reqCode)
    {

        String url = Constants.HOST_URL + urlSuffix;
        if(Constants.DEBUG) Log.d(TAG,"URL --"+url);
        if(Constants.DEBUG) Log.d(TAG,"post params body --"+postParams.toString());

        VolleyCustomRequest request = new VolleyCustomRequest(Request.Method.POST, url, (Map<String, String>) postParams,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if (Constants.DEBUG) Log.d(TAG + ": ", "somePostRequest Response : " + response.toString());
                        if(null != response.toString())
                            listener.getResult(reqCode,response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                            listener.getErrorResult(parseErrorResp(error));

                    }
                });

        mRequestQueue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void postListRequest(String urlSuffix, Object postParams, final VolleyCallbackListener<Object> listener, final int reqCode)
    {

        String url = Constants.HOST_URL + urlSuffix;
        if(Constants.DEBUG) Log.d(TAG,"URL --"+url);
        if(Constants.DEBUG) Log.d(TAG,"post params body --"+postParams.toString());

        VolleyCustomRequest request = new VolleyCustomRequest(Request.Method.POST, url, (Map<String, String>) postParams,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if (Constants.DEBUG) Log.d(TAG + ": ", "somePostRequest Response : " + response.toString());
                        if(null != response.toString())
                            listener.getResult(reqCode,response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                            listener.getErrorResult(parseErrorResp(error));

                    }
                });

        mRequestQueue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    
  
    public void getRequest(String urlSuffix, Object postParams, final VolleyCallbackListener<Object> listener, final int reqCode)
    {

        String url = Constants.HOST_URL + urlSuffix;
        if(Constants.DEBUG) Log.d(TAG,"URL --"+url);
//        if(Constants.DEBUG) Log.d(TAG,"post params body --"+postParams.toString());

        VolleyCustomRequest request = new VolleyCustomRequest(Request.Method.GET, url, (Map<String, String>) postParams,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if (Constants.DEBUG) Log.d(TAG + ": ", "somePostRequest Response : " + response.toString());
                        if(null != response.toString())
                            listener.getResult(reqCode,response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        listener.getErrorResult(parseErrorResp(error));
                    }
                });

        mRequestQueue.add(request);
    }

    private String parseErrorResp(VolleyError error){
        String errorMessage = null;
        if (null != error.networkResponse)
        {
            if (Constants.DEBUG) Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
            byte[] errRespData = error.networkResponse.data;
            String errorResp = new String(errRespData); // for UTF-8 encoding
            if (Constants.DEBUG) Log.d(TAG + ": ", "Error Response data: " + errorResp);
            if(errorResp !=null){
                String resp = (String) errorResp;
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    errorMessage = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }else{
            if(error.getCause()!=null)
            errorMessage = error.getCause().getMessage();

        }
        return errorMessage;
    }
}
