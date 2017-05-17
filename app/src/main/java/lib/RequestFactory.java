package lib;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.lang.reflect.Array;
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
import static ep1.joaofran.com.ep1.R.string.signup;

/**
 * Created by joaofran on 27/04/17.
 */

public class RequestFactory {

    // para facilitar, em cada request é apenas acrescentado a URI correspondente
    private final String base_url = "http://207.38.82.139:8001/";

    /**
     * Cria uma request do tipo GET para a lista de seminários já cadastrados.
     * Assim que o método é chamado, ele cria uma ProgressDialog para que o
     * usuário tenha ciência de que algo está em processo.
     * A request, ao obter uma resposta positiva do servidor, submete a informação
     * recebida ao método 'treatGETSeminarRequestResponse'; caso contrário, numa
     * resposta negativa, cancela a Dialog e informa o usuário do erro ocorrido.
     */
    public JsonObjectRequest GETSeminarList(final List<Seminar> seminars, final ArrayAdapter adapter,
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
                Log.d(TAG, "GETSeminarRequest failed");
                progressDialog.dismiss();
                alertMsg(context, R.string.request_failure);
            }
        });
    }

    /**
     * Destrincha o json obtido em vários jsons, os insere num ArrayList
     * e notifica o Adapter (da ListView) que houve alterações, para que
     * a ListView seja atualizada com o novo conteúdo.
     */
    private void treatGETSeminarRequestResponse(JSONObject response, List<Seminar> seminars, ArrayAdapter adapter) {
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
            Log.e(TAG, String.valueOf(e.getStackTrace()));
        }
    }

    /**
     * Cria uma request do tipo POST para criar um novo seminário no WebService.
     * Ao receber uma resposta, notifica o usuário da situação (sucesso ou fracasso)
     * através de um Toast
     */
    public StringRequest POSTNewSeminarRequest(final Context context, final Map<String, String> params) {
        final String TAG = "POSTNewSeminarRequest";
        String url = this.base_url + "seminar/add";
        return new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, params.get((String) "name").toString() + R.string.seminar_created_succsess, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "POSTNewSeminarRequest failed");
                Toast.makeText(context, R.string.request_failure, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }

    /**
     * Cria uma request do tipo POST para solicitar os alunos inscritos num
     * determinado seminário.
     * Ao receber uma resposta positiva, faz uma nova solicitação ao WebService
     * pelo nome do usuário para cada NUSP obtido, para que a exibição da
     * informação seja completa; caso a resposta não contenha nenhum aluno,
     * informa ao usuário com um texto no lugar da lista de alunos; caso a resposta
     * seja negativa, informa o usuário do ocorrido
     */
    public StringRequest POSTStudentsEnrolled(final Context context, final List<User> students, final ArrayAdapter adapter, final Map<String, String> params) {
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
                                User user = new User((String) jsonObject.get("student_nusp"), true);
                                VolleySingleton.getInstance(context).addToRequestQueue(
                                        new RequestFactory().GETUserName(context, user, students, adapter)
                                );
                            }
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            User user = new User("", true);
                            user.setName(context.getString(R.string.no_students_enrolled));
                            students.add(user);
                            adapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "failed");
                        alertMsg(context, R.string.request_failure);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }

    /**
     * Cria uma request do tipo POST para remover um seminário.
     * Em caso de erro, notifica o usuário através de um toast
     */
    public StringRequest POSTDeleteSeminar(final Map<String, String> params, final Context context) {
        final String TAG = "POSTDeleteSeminar";
        String url = this.base_url + "seminar/delete";
        return new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("POSTDeleteSeminar", "delete success");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "delete failed");
                Toast.makeText(context, R.string.request_failure, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }

    /**
     * Cria uma request do tipo POST para edição de um seminário.
     * Notifica o usuário em caso de erro
     */
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
                alertMsg(context, R.string.request_failure);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }

    /**
     * Cria uma request do tipo POST para realizar o login no sistema.
     * Enquanto não recebe uma resposta, cria uma ProgressDialog para informar
     * o usuário que está sendo processado algo. Ao receber uma resposta positiva,
     * faz a persistência do login pelo uso de uma SharedPreferences e já aciona
     * a nova activity - ProfileActivity -, onde há a lista de seminários; caso
     * haja uma resposta negativa, notifica o usuário do ocorrido
     */
    public StringRequest POSTLogin(final Context context, final String login, String pass,
                                   final boolean is_student, final SharedPreferences.Editor prefs_editor) {
        final String TAG = "POSTLogin";
        String url = this.base_url + "login/" + (is_student ? "student" : "teacher");

        Log.d(TAG, "url: " + url);

        final Map<String, String> params = new HashMap<>();
        params.put("nusp", login);
        params.put("pass", pass);

        String message = context.getString(R.string.authentication);

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.show();

        return new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("true")) {
                            Log.d(TAG, "login successful");
                            prefs_editor.putString(User.ID, login);
                            prefs_editor.putBoolean(User.TYPE, is_student);
                            prefs_editor.commit();
                            Intent intent = new Intent(context, ProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);

                            progressDialog.dismiss();

                            ((Activity) context).finish();
                        } else {
                            Log.d(TAG, "login failed");
                            progressDialog.dismiss();

                            alertMsg(context, R.string.login_fail);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.getMessage());
                alertMsg(context, R.string.request_failure);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }


    /**
     * Cria uma request do tipo POST para criar uma nova conta.
     * Exibe uma ProgressDialog enquanto espera uma resposta do
     * WebService. Caso a resposta seja positiva, faz login com
     * persistência (SharedPreferences)
     */
    public StringRequest POSTSignUp(final Context context, final String name, final String login,
                                    String pass, final boolean is_student, final SharedPreferences.Editor prefs_editor) {
        final String TAG = "POSTSignUp";
        String url = this.base_url + (is_student ? "student" : "teacher") + "/add";

        Log.d(TAG, "url: " + url);

        final Map<String, String> params = new HashMap<>();
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
                        Log.d(TAG, response);
                        if (response.contains("true")) {
                            Log.d(TAG, "signup successful");
                            prefs_editor.putString(User.ID, login);
                            prefs_editor.putBoolean(User.TYPE, is_student);
                            prefs_editor.commit();
                            progressDialog.dismiss();
                            context.startActivity(new Intent(context, ProfileActivity.class));
                        } else {
                            Log.d(TAG, "signup failed");
                            progressDialog.dismiss();
                            alertMsg(context, R.string.signup_fail);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.getMessage());
                alertMsg(context, R.string.request_failure);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }

    /**
     * Cria uma request do tipo POST para matricular um aluno num seminário.
     * No caso do aluno estar sendo confirmado por um professor, recebe também a lista de alunos e
     * seu adapter, para quando receber uma resposta positiva, faz uma nova request pelo nome do
     * aluno e o insere na lista de matriculados.
     */
    public StringRequest POSTEnroll(final Context context, final String user_id, final String seminar_id,
                                    final List<User> students, final ArrayAdapter adapter) {
        final String TAG = "POSTEnroll";
        String url = base_url + "attendence/submit";

        final Map<String, String> params = new HashMap<>();

        Log.d(TAG, "NUSP: " + user_id);
        Log.d(TAG, "Seminar: " + seminar_id);
        params.put("nusp", user_id);
        params.put("seminar_id", seminar_id);

        return new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("true")) {
                            Log.d(TAG, "enroll success");
                            alertMsg(context, R.string.enroll_success);
                            if (students != null) {
                                User user = new User(user_id, true);
                                VolleySingleton.getInstance(context).addToRequestQueue(
                                        new RequestFactory().GETUserName(context, user, students, adapter)
                                );
                            }
                        } else {
                            Log.d(TAG, "enroll fail");
                            alertMsg(context, R.string.enroll_fail);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.getMessage());
                alertMsg(context, R.string.request_failure);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }

    /**
     * Cria uma request do tipo POST para editar o perfil de um aluno.
     * Ao receber uma resposta positiva, inicia uma nova Activity, onde
     * terá a lista de seminários novamente.
     */
    public StringRequest POSTEditUser(final Context context, String nusp, Boolean is_student, String name, String pass) {
        String url = this.base_url + (is_student ? "student" : "teacher") + "/edit";
        final String TAG = "˜nt";

        final Map<String, String> params = new HashMap<>();
        params.put("nusp", nusp);
        params.put("name", name);
        params.put("pass", pass);

        return new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "sucess");
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle(context.getString(R.string.edit_success));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(context, ProfileActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        context.startActivity(intent);
                                    }
                                });
                        alertDialog.show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Failed");
                alertMsg(context, R.string.request_failure);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }

    /**
     * Chamada quando um professor tenta confirmar um aluno. Cria uma request do tipo GET para confirmar
     * que o aluno sendo confirmado é um aluno válido. Ao receber uma resposta positiva chama a
     * função de confirmação (POSTEnroll) passando a lista de alunos e seu adapter.
     */
    public StringRequest ConfirmStudent(final Context context, final String nusp, final String seminar_id,
                                        final List<User> students, final ArrayAdapter adapter) {
        final String TAG = "ConfirmStudent";
        String url = this.base_url + "student/get/" + nusp;
        String message = context.getString(R.string.authentication);

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.show();

        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (response.contains("true")) {
                            Log.d(TAG, "Student");

                            VolleySingleton.getInstance(context).addToRequestQueue(
                                    new RequestFactory().POSTEnroll(context, nusp, seminar_id, students, adapter)
                            );
                        } else {
                            alertMsg(context, R.string.no_account);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "GET Student failed");
                progressDialog.dismiss();
                alertMsg(context, R.string.no_account);
            }
        });
    }

    /**
     * Chamada quando um aluno tenta se matricular em um seminario. Cria uma request do tipo GET para confirmar
     * que o seminario é válido. Ao receber uma resposta positiva chama a
     * função de confirmação (POSTEnroll).
     */
    public StringRequest ConfirmSeminar(final Context context, final String nusp, final String seminar_id) {
        final String TAG = "ConfirmSeminar";
        String url = this.base_url + "seminar/get/" + seminar_id;
        String message = context.getString(R.string.authentication);

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.show();

        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (response.contains("true")) {
                            Log.d(TAG, "Seminar");

                            VolleySingleton.getInstance(context).addToRequestQueue(
                                    new RequestFactory().POSTEnroll(context, nusp, seminar_id, null, null)
                            );
                        } else {
                            alertMsg(context, R.string.no_seminar);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "No seminar");
                progressDialog.dismiss();
                alertMsg(context, R.string.no_seminar);
            }
        });
    }

    /**
     * Cria uma request do tipo GET para pegar informações de um aluno,
     * dado seu número USP.
     */
    public JsonObjectRequest GETUserName(final Context context, final User user, final List<User> students, final ArrayAdapter adapter) {
        final String TAG = "GETUserName";
        Log.d(TAG, "In GetUserName");
        String url = this.base_url + (user.isStudent() ? "student" : "teacher") + "/get/" + user.getId();

        return new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, "Success");
                            user.setName(response.getJSONObject("data").getString("name"));
                            students.add(user);
                            Log.d(TAG, user.toString());
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "failed");
                alertMsg(context, R.string.request_failure);
            }
        });
    }

    private void alertMsg(Context context, int string_id) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getString(string_id));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
