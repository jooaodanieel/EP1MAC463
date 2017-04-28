package lib;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import ep1.joaofran.com.ep1.R;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by joaofran on 27/04/17.
 */

public class RequestFactory {

    private final String base_url = "http://207.38.82.139:8001/";
    private String debugTag;


    public JsonObjectRequest GETSeminarList (final List<Seminar> seminars, final ArrayAdapter adapter, final String debugTag) {
        String url = this.base_url + "seminar";
        return new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        treatGETSeminarRequestResponse(response, seminars, adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(debugTag,"GETSeminarRequest failed");
            }
        });
    }

    // para clareza: gera uma request mas NÃO ZERA a lista de Seminars atual
    public StringRequest POSTNewSeminarRequest (final Context context, final Map<String,String> params) {
        String url = this.base_url + "seminar/add";
        return new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, params.get((String)"name").toString() + R.string.seminar_created_succsess, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(debugTag, "POSTNewSeminarRequest failed");
                Toast.makeText(context, R.string.seminar_created_faliure, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }

    public JsonObjectRequest POSTStudentList(final Context context, final  Map<String, String> params,
                                              final List<String> students, final ArrayAdapter adapter) {
        Log.d("HERE", "in POSTStudentList");
        String url = this.base_url + "attendence/listStudents";
        return new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        treatPOSTStudentRequestResponse(response, students, adapter);
                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("HERE", "POSTStudentList failed");
            }
        });
    }

    private void treatGETSeminarRequestResponse (JSONObject request, List<Seminar> seminars, ArrayAdapter adapter) {
        try {
            if (request != null) {
                for (int i = 0; i < ((JSONArray) request.get("data")).length(); i++) {
                    JSONObject jsob = ((JSONArray) request.get("data")).getJSONObject(i);
                    seminars.add(new Seminar(jsob.getInt("id"), jsob.getString("name")));
                    Log.d(TAG, "Added " + jsob.getInt("id") + jsob.getString("name"));
                }
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
//            e.printStackTrace();
            Log.e(TAG, String.valueOf(e.getStackTrace()));
        }
    }


    private void treatPOSTStudentRequestResponse (JSONObject request, List<String> students, ArrayAdapter adapter) {
        Log.d("HERE", request.toString());
        try {
            if (request != null) {
                for (int i = 0; i < ((JSONArray) request.get("data")).length(); i++) {
                    JSONObject jsob = ((JSONArray) request.get("data")).getJSONObject(i);
                    students.add(jsob.getString("nusp"));
                    Log.d("HERE", "Added " + jsob.getString("nusp"));
                }
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            Log.e("HERE", String.valueOf(e.getStackTrace()));
        }
    }

}
