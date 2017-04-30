package lib;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ep1.joaofran.com.ep1.ProfileActivity;
import ep1.joaofran.com.ep1.R;
import ep1.joaofran.com.ep1.SeminarActivity;
import ep1.joaofran.com.ep1.SignUpActivity;

import static com.android.volley.VolleyLog.TAG;
import static ep1.joaofran.com.ep1.R.string.login;

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
        Log.d("HERE", String.valueOf(params));
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }

    private void treatGETSeminarRequestResponse (JSONObject response, List<Seminar> seminars, ArrayAdapter adapter) {
        try {
            if (response != null) {
                for (int i = 0; i < ((JSONArray) response.get("data")).length(); i++) {
                    JSONObject jsob = ((JSONArray) response.get("data")).getJSONObject(i);
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
//        try {
            if (request != null) {
                Log.d("ashusadu", String.valueOf(request));
//                for (int i = 0; i < ((JSONArray) request.get("data")).length(); i++) {
//                    JSONObject jsob = ((JSONArray) request.get("data")).getJSONObject(i);
//                    students.add(jsob.getString("nusp"));
//                    Log.d("HERE", "Added " + jsob.getString("nusp"));
//                }
//                adapter.notifyDataSetChanged();
            }
//        } catch (JSONException e) {
//            Log.e("ashusadu", e.getMessage());
//        }
    }


    public StringRequest POSTDeleteSeminar (/*final Context context, */final Map<String,String> params) {
        String url = this.base_url + "seminar/delete";
        return new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("POSTDeleteSeminar","delete success");
//                        context.startActivity(new Intent(context,ProfileActivity.class));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("POSTDeleteSeminar", "delete failed");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }

    public StringRequest POSTEditSeminar(final Map<String, String> params) {
        String url = base_url + "seminar/edit";
        return new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("POSTEditSeminar", "edit succsess");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("POSTEditSeminar", "edit failure");
            }
        }) {
            @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
            }
        };
    }

    //    public StringRequest POSTNewStudent (final Context context, final Map<String,String> params) {
    //        String url = this.base_url + "student/add";
    //        return new StringRequest(Request.Method.POST, url,
    //                new Response.Listener<String>() {
    //                    @Override
    //                    public void onResponse(String response) {
    //                        //
    //                        Toast.makeText(context,R.string.new_user_success,Toast.LENGTH_SHORT).show();
    //                        Intent intent = new Intent(context, ProfileActivity.class);
    //                        intent.putExtra(User.ID, params.get());
    //                        intent.putExtra(User.TYPE, true);
    //                        context.startActivity(intent);
    //                    }
    //                }, new Response.ErrorListener() {
    //            @Override
    //            public void onErrorResponse(VolleyError error) {
    //                //
    //            }
    //        }){
    //            @Override
    //            protected Map<String, String> getParams() throws AuthFailureError {
    //                return params;
    //            }
    //        };
    //    }

    public StringRequest POSTLogin (final Context context, final String login, String pass, final boolean is_student, final SharedPreferences.Editor prefs_editor) {
        final String TAG = "LOGINREQ";
        String url = this.base_url + "login/" + (is_student ? "student" : "teacher");

        Log.d(TAG,"url: " + url);

        final Map<String,String> params = new HashMap<>();
        params.put("nusp",login);
        params.put("pass",pass);

        return new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d(TAG,response);
                        if (response.contains("true")) {
                            Log.d(TAG,"login successful");
                            prefs_editor.putString(User.ID,login);
                            prefs_editor.putBoolean(User.TYPE,is_student);
                            prefs_editor.commit();
                            context.startActivity(new Intent(context,ProfileActivity.class));
                        } else {
                            Log.d(TAG,"login failed");
                            Toast.makeText(context,context.getString(R.string.login_fail),Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }
}
