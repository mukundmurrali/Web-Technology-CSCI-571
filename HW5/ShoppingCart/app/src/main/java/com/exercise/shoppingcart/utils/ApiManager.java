package com.exercise.shoppingcart.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by mukundmurrali on 4/24/2019.
 */

public class ApiManager {
    private static ApiManager mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    public ApiManager(Context ctx) {
        mCtx = ctx;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized ApiManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiManager(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static void call(Context ctx, String query, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        String url = AppConstants.BASE_URL + AppConstants.GET_ZIPCODES_ENDPOINT + "?postalCode="+ query;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        ApiManager.getInstance(ctx).addToRequestQueue(stringRequest);
    }
}
