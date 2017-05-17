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
 *
 * Singleton criado para que haja unicidade da instância
 * da RequestQueue e persistência no ciclo de vida das
 * Activities
 *
 */

public class VolleySingleton {
    private static VolleySingleton mInstance;
    private static Context mContext;
    private RequestQueue mRequestQueue;

    private VolleySingleton (Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    /**
     * Retorna a instância atual do Singleton ou cria
     * uma nova (caso nenhuma tenha sido instanciada)
     */
    public static synchronized VolleySingleton getInstance (Context context) {
        if (mInstance == null)
            mInstance = new VolleySingleton(context);
        return mInstance;
    }

    /**
     * Retorna a instância atual da RequestQueue ou cria
     * uma nova (caso nenhuma tenha sido instanciada)
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        return mRequestQueue;
    }

    /**
     * Adiciona Request req à RequestQueue
     */
    public <T> void addToRequestQueue (Request<T> req) {
        getRequestQueue().add(req);
    }

}
