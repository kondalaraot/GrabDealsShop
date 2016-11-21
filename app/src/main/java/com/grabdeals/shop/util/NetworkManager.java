package com.grabdeals.shop.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by KTirumalsetty on 10/31/2016.
 */

public class NetworkManager
{
    private static final String TAG = "NetworkManager";
    private static NetworkManager instance = null;

//    private static final String prefixURL = "http://some/url/prefix/";

    //for Volley API
    public RequestQueue requestQueue;

    private NetworkManager(Context context)
    {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        //other stuf if you need
    }

    public static synchronized NetworkManager getInstance(Context context)
    {
        if (null == instance)
            instance = new NetworkManager(context);
        return instance;
    }

    //this is so you don't need to pass context each time
    public static synchronized NetworkManager getInstance()
    {
        if (null == instance)
        {
            throw new IllegalStateException(NetworkManager.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public void postRequest(String urlSuffix,Object postParams, final VolleyCallbackListener<Object> listener)
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
                            listener.getResult(response);
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

        requestQueue.add(request);
    }
    
  
    public void getRequest(String urlSuffix,Object postParams, final VolleyCallbackListener<Object> listener)
    {

        String url = Constants.HOST_URL + urlSuffix;
        if(Constants.DEBUG) Log.d(TAG,"URL --"+url);
        if(Constants.DEBUG) Log.d(TAG,"post params body --"+postParams.toString());

        VolleyCustomRequest request = new VolleyCustomRequest(Request.Method.GET, url, (Map<String, String>) postParams,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if (Constants.DEBUG) Log.d(TAG + ": ", "somePostRequest Response : " + response.toString());
                        if(null != response.toString())
                            listener.getResult(response);
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

        requestQueue.add(request);
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
