package com.example.marriagehall;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private Context mContext;

    //PRIVATE Constructor of the class
    private VolleySingleton(Context context){
        this.mContext = context;
    }

    //static function which creates the object of the class
    //here this function ensures that there should be only one object is created at a time
    public static synchronized VolleySingleton getInstance(Context context){
        if(mInstance == null)
            mInstance = new VolleySingleton(context);
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null)
        {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
