package ep1.joaofran.com.ep1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.content.DialogInterface;
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

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lib.RequestFactory;
import lib.Seminar;
import lib.User;
import lib.VolleySingleton;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private ListView seminars_list;
    private ArrayList<Seminar> seminars;
    private ArrayAdapter adapter;
    private final RequestFactory factory = new RequestFactory();

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefs_editor;


    // Action Bar
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.itLogOut):
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case (R.id.refresh):
                recreate();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(R.string.profile_title);

        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(getString(R.string.shared_preferences_file),MODE_PRIVATE);
        prefs_editor = prefs.edit();

//        Intent intent = getIntent();
//        String u_num = intent.getStringExtra(User.ID);
//        Boolean u_student = intent.getBooleanExtra(User.TYPE, true);

        String u_num = prefs.getString(User.ID,"default");
        Boolean u_student = prefs.getBoolean(User.TYPE,true);

        if (u_student) {
            Log.d(TAG, "In Profile activity: Student");

            setContentView(R.layout.activity_student);
            this.seminars_list = (ListView) findViewById(R.id.lvStudentSeminar);
        }
        else {
            Log.d(TAG, "In Profile activity: Teacher");

            setContentView(R.layout.activity_teacher);
            this.seminars_list = (ListView) findViewById(R.id.lvTeacherSeminar);
        }

        listSetup(u_num, u_student);
    }


    private void listSetup(String num, Boolean student) {
        Log.d(TAG, "Setting up seminar list");

        Toast.makeText(this,R.string.seminars_retrieval, Toast.LENGTH_LONG).show();

        final String u_num = num;
        final Boolean u_student = student;

        this.seminars = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, R.layout.seminar_row, R.id.tvSeminar, this.seminars);
        seminars_list.setAdapter(adapter);

        seminars_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent item_click = new Intent(ProfileActivity.this, SeminarActivity.class);
                item_click.putExtra(User.ID, u_num);
                item_click.putExtra(User.TYPE, u_student);
                item_click.putExtra(Seminar.ID, seminars.get(position).getId().toString());
                item_click.putExtra(Seminar.NAME, seminars.get(position).getName());
                startActivity(item_click);
            }

        });

//        String url = "http://207.38.82.139:8001/seminar";
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        treatRequestResponse(response,adapter);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d(TAG,"JSONRequest failed");
//            }
//        });

        JsonObjectRequest request = factory.GETSeminarList(seminars,adapter,TAG);
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void addSeminar(final View view) {
        Log.d(TAG, "Adding new seminar");

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.newSeminar);

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                // Entender como atribuir PRIORIDADES às requests
                // POST precisa ser feito antes do GET
                Map<String,String> params = new HashMap<>();
                params.put("name",input.getText().toString());
                StringRequest POSTRequest = factory.POSTNewSeminarRequest(view.getContext(),params);
                VolleySingleton.getInstance(view.getContext()).addToRequestQueue(POSTRequest);
                recreate();

            }
        });

        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelado
            }
        });

        alert.show();
    }
}
