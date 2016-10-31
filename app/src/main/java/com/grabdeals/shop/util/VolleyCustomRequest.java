package com.grabdeals.shop.util;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

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

    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public VolleyCustomRequest(String url, Map<String, String> params,
                               Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = reponseListener;
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
            /*Map<String, String> params = new HashMap<String, String>();
		params.put("Content-Type","application/json");
		params.put("Accept", "application/json");*/
//		params.put("X-client-identifier", UserInfo.getDeviceId());
            //params.put("X-client-identifier", UserInfo.getUAChannelID());
//		params.put("X-client-version", UserInfo.getAndroidVersion());
//		params.put("X-client-platform", UserInfo.getPlatform());
//		params.put("X-client-type", UserInfo.getClientType());


		/*TimeZone tz	= TimeZone.getDefault();
		Date now	= new Date();
		String offsetFromUtc	= "" + (tz.getOffset(now.getTime()) / 1000);
		System.out.println("Offset" + offsetFromUtc);
//		params.put("X-client-timezone", offsetFromUtc);*/
        }

//            AppController.getInstance().addSessionCookie(headers);

        return headers;
    }
}
