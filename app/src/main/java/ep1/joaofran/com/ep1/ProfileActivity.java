package ep1.joaofran.com.ep1;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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

import lib.QRCodeManager;
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
                // clear no shared preferences
                prefs_editor.clear();
                prefs_editor.commit();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            case (R.id.refresh):
                JsonObjectRequest request = factory.GETSeminarList(seminars, adapter, ProfileActivity.this);
                VolleySingleton.getInstance(this).addToRequestQueue(request);
                return true;
            case (R.id.edit):
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(R.string.profile_title);

        super.onCreate(savedInstanceState);

        // recupera usuário logado
        prefs = getSharedPreferences(getString(R.string.shared_preferences_file),MODE_PRIVATE);
        prefs_editor = prefs.edit();

        String u_num = prefs.getString(User.ID,"default");
        Boolean u_student = prefs.getBoolean(User.TYPE,true);

        // verifica o perfil do usuário: professor/aluno
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

        // carrega lista de seminários do WebService
        listSetup(u_num, u_student);
    }


    private void listSetup(String num, Boolean student) {
        Log.d(TAG, "Setting up seminar list");

        final String u_num = num;
        final Boolean u_student = student;

        this.seminars = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, R.layout.seminar_row, R.id.tvSeminar, this.seminars);
        seminars_list.setAdapter(adapter);

        // caso seja professor, define que click em item da lista carrega SeminarActivity
        if (!student) {
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
        }

        // faz a request no WebService
        JsonObjectRequest request = factory.GETSeminarList(seminars, adapter, ProfileActivity.this);
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    /**
     * Callback do floating button quando usuário é professor
     */
    public void addSeminar(final View view) {
        Log.d(TAG, "Adding new seminar");

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.newSeminar);

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

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

    /**
     * Callback do floating button quando usuário é aluno
     */
    public void scanQR(View v) {
        try {
            Intent intent = new Intent(QRCodeManager.ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(this, R.string.no_scanner, Toast.LENGTH_LONG);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String content = intent.getStringExtra("SCAN_RESULT");
                VolleySingleton.getInstance(ProfileActivity.this).addToRequestQueue(
                        factory.ConfirmSeminar(ProfileActivity.this, prefs.getString(User.ID, "default"), content)
                );
            }
        }
    }
}
