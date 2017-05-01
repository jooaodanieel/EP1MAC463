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

    public StringRequest POSTStudentsEnrolled (final Context context, final List<String> students, final ArrayAdapter adapter, final Map<String,String> params) {
        final String TAG = "EnrolledStudentsRequest";
        String url = this.base_url + "attendence/listStudents";

        return new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray data = (JSONArray) jsonObject.get("data");
                            for (int i = 0; i < data.length(); i++) {
                                jsonObject = (JSONObject) data.get(i);
                                students.add((String) jsonObject.get("student_nusp"));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.d(TAG,e.getMessage());
                            students.add(context.getString(R.string.no_students_enrolled));
                            adapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,"failed");
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }

    public StringRequest POSTDeleteSeminar (final Map<String,String> params) {
        String url = this.base_url + "seminar/delete";
        return new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("POSTDeleteSeminar","delete success");
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

    public StringRequest POSTSignUp (final Context context, final String name, final String login, String pass, final boolean is_student, final SharedPreferences.Editor prefs_editor) {
        final String TAG = "SIGNUPREQ";
        String url = this.base_url + (is_student ? "student" : "teacher") +  "/add";

        Log.d(TAG,"url: " + url);

        final Map<String,String> params = new HashMap<>();
        params.put("name", name);
        params.put("nusp", login);
        params.put("pass", pass);

        return new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,response);
                        if (response.contains("true")) {
                            Log.d(TAG,"signup successful");
                            prefs_editor.putString(User.ID,login);
                            prefs_editor.putBoolean(User.TYPE,is_student);
                            prefs_editor.commit();
                            context.startActivity(new Intent(context,ProfileActivity.class));
                        } else {
                            Log.d(TAG,"signup failed");
                            Toast.makeText(context,context.getString(R.string.signup_fail),Toast.LENGTH_SHORT).show();
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

    public StringRequest POSTEnroll (final Context context, final String user_id, final String seminar_id) {
        final String TAG = "POSTEnroll";
        String url = base_url + "attendence/submit";

        final Map<String, String> params = new HashMap<>();

        params.put("nusp", user_id);
        params.put ("seminar_id", seminar_id);

        return new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("true")) {
                            Log.d(TAG, "enroll success");
                            Toast.makeText(context, R.string.enroll_success, Toast.LENGTH_LONG).show();
                        }
                        else {
                            Log.d(TAG, "enroll fail");
                            Toast.makeText(context, R.string.enroll_fail, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG,error.getMessage());
                }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }
}
