package lib;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by joaofran on 26/04/17.
 */

public class VolleySingleton {
    private static VolleySingleton mInstance;
    private static Context mContext;
    private RequestQueue mRequestQueue;


    private VolleySingleton (Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance (Context context) {
        if (mInstance == null)
            mInstance = new VolleySingleton(context);
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        return mRequestQueue;
    }

    public <T> void addToRequestQueue (Request<T> req) {
        getRequestQueue().add(req);
    }

}
