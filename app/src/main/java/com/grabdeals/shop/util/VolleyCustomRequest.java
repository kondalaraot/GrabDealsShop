package com.grabdeals.shop.util;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.grabdeals.shop.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KTirumalsetty on 10/31/2016.
 */

public class VolleyCustomRequest extends Request<JSONObject> {

    private static final String TAG ="VolleyCustomRequest" ;
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;
    private PreferenceManager manager = new PreferenceManager(MyApplication.getAppContext());
    public VolleyCustomRequest(String url, Map<String, String> params,
                               Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = responseListener;
        this.params = params;
    }

    public VolleyCustomRequest(int method, String url, Map<String, String> params,
                               Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    };

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//            AppController.getInstance().checkSessionCookie(response.headers);
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    /*@Override
    protected VolleyError parseNetworkError(VolleyError volleyError){
        if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
            volleyError = error;
        }

        return volleyError;
    }*/


    @Override
    protected void deliverResponse(JSONObject response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();

		/*TimeZone tz	= TimeZone.getDefault();
		Date now	= new Date();
		String offsetFromUtc	= "" + (tz.getOffset(now.getTime()) / 1000);
		System.out.println("Offset" + offsetFromUtc);
//		params.put("X-client-timezone", offsetFromUtc);*/
            StringBuilder sb = new StringBuilder();
            sb.append(UserAgent.getAppCode());
            sb.append(";");
            sb.append(UserAgent.getAppVersion());
            sb.append(";");
            sb.append(UserAgent.getOsName());
            sb.append(";");
            sb.append(UserAgent.getOsVersion());
            sb.append(";");
            sb.append(UserAgent.getDeviceMake());
            sb.append(";");
            sb.append(UserAgent.getDeviceModel());
            sb.append(";");
            sb.append(UserAgent.getNotificationID());
            sb.append(";");
            sb.append(UserAgent.getMacAddr());
            String userAgent = sb.toString();
            if (Constants.DEBUG) Log.d(TAG,"userAgent--->"+userAgent );
            if (Constants.DEBUG) Log.d(TAG,"authorization--->"+manager.getAuthToken() );
            headers.put("authorization",manager.getAuthToken());
            headers.put("user_agent",userAgent);
            headers.put("Content-Type","application/x-www-form-urlencoded");
            headers.put("Accept", "application/json");
        }

//            AppController.getInstance().addSessionCookie(headers);

        return headers;
    }
}
