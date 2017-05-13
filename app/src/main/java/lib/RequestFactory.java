package lib;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import ep1.joaofran.com.ep1.EditProfileActivity;
import ep1.joaofran.com.ep1.LoginActivity;
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


    public JsonObjectRequest GETSeminarList (final List<Seminar> seminars, final ArrayAdapter adapter,
                                             final Context context) {
        final String TAG = "GETSeminarList";
        String url = this.base_url + "seminar";
        String message = context.getString(R.string.seminars_retrieval);

        final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(message);
                progressDialog.show();

        return new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        treatGETSeminarRequestResponse(response, seminars, adapter);
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"GETSeminarRequest failed");
                progressDialog.dismiss();
                Toast.makeText(context, R.string.request_failure, Toast.LENGTH_SHORT).show();
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
        final String TAG = "POSTNewSeminarRequest";
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
                Log.d(TAG, "POSTNewSeminarRequest failed");
                Toast.makeText(context, R.string.request_failure, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }

    public StringRequest POSTStudentsEnrolled (final Context context, final List<String> students, final ArrayAdapter adapter, final Map<String,String> params) {
        final String TAG = "POSTStudentsEnrolled";
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
                        Toast.makeText(context, R.string.request_failure, Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }

    public StringRequest POSTDeleteSeminar (final Map<String,String> params, final Context context) {
        final String TAG = "POSTDeleteSeminar";
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
                Log.d(TAG, "delete failed");
                Toast.makeText(context, R.string.request_failure, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }

    public StringRequest POSTEditSeminar(final Map<String, String> params, final Context context) {
        final String TAG = "POSTEditSeminar";
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
                Log.d(TAG, "edit failure");
                Toast.makeText(context, R.string.request_failure, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
            }
        };
    }

    public StringRequest POSTLogin (final Context context, final String login, String pass,
                                    final boolean is_student, final SharedPreferences.Editor prefs_editor) {
        final String TAG = "POSTLogin";
        String url = this.base_url + "login/" + (is_student ? "student" : "teacher");

        Log.d(TAG,"url: " + url);

        final Map<String,String> params = new HashMap<>();
        params.put("nusp",login);
        params.put("pass",pass);

        String message = context.getString(R.string.authentication);

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.show();

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
                            Intent intent = new Intent(context, ProfileActivity.class);// New activity
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);

                            progressDialog.dismiss();

                            ((Activity) context).finish();
                        } else {
                            Log.d(TAG,"login failed");
                            progressDialog.dismiss();

                            Toast.makeText(context,context.getString(R.string.login_fail),Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,error.getMessage());
                Toast.makeText(context, R.string.request_failure, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }

    public StringRequest POSTSignUp (final Context context, final String name, final String login,
                                     String pass, final boolean is_student, final SharedPreferences.Editor prefs_editor) {
        final String TAG = "POSTSignUp";
        String url = this.base_url + (is_student ? "student" : "teacher") +  "/add";

        Log.d(TAG,"url: " + url);

        final Map<String,String> params = new HashMap<>();
        params.put("name", name);
        params.put("nusp", login);
        params.put("pass", pass);

        String message = context.getString(R.string.creating_accont);

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.show();

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
                            progressDialog.dismiss();
                            context.startActivity(new Intent(context,ProfileActivity.class));
                        } else {
                            Log.d(TAG,"signup failed");
                            progressDialog.dismiss();
                            Toast.makeText(context,context.getString(R.string.signup_fail),Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,error.getMessage());
                Toast.makeText(context, R.string.request_failure, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, R.string.request_failure, Toast.LENGTH_SHORT).show();
                }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }

    public StringRequest POSTEditStudent (final Context context, String nusp, String name, String pass) {
        String url = this.base_url + "student/edit";
        final String TAG = "POSTEditStudent";

        final Map<String,String> params = new HashMap<>();
        params.put("nusp",nusp);
        params.put("name",name);
        params.put("pass",pass);

        return new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"sucess");
                        Toast.makeText(context,context.getString(R.string.edit_success),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, ProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"Failed");
                Toast.makeText(context, R.string.request_failure, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }
}
