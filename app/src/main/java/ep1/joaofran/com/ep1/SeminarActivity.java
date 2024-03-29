package ep1.joaofran.com.ep1;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.WriterException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lib.QRCodeManager;
import lib.RequestFactory;
import lib.Seminar;
import lib.User;
import lib.VolleySingleton;

public class SeminarActivity extends AppCompatActivity {

    private static final String TAG = "SeminarActivity";


    private TextView tv_seminar_name;
    private String seminar_id;
    private String seminar_name;
    private Boolean is_student;
    private Bitmap bitmap = null;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefs_editor;

    // Elementos da activity de um professor
    private EditText et_seminar_name;
    private ListView students_list;
    private ArrayList<User> students;
    private ArrayAdapter adapter;
    private final RequestFactory factory = new RequestFactory();

    // Action Bar
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu){
        menu.findItem(R.id.refresh).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.itLogOut):
                // apaga sessão
                prefs_editor.clear();
                prefs_editor.commit();
                Intent intent = new Intent(SeminarActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            case (R.id.edit):
                startActivity(new Intent(SeminarActivity.this, EditProfileActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.seminar_title);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        prefs = getSharedPreferences(getString(R.string.shared_preferences_file),MODE_PRIVATE);
        prefs_editor = prefs.edit();

        Intent intent = getIntent();
        is_student = intent.getBooleanExtra(User.TYPE, true);
        seminar_id = intent.getStringExtra(Seminar.ID);
        seminar_name = intent.getStringExtra(Seminar.NAME);

        // activity que só tem sentido se usuário for professor
        if (is_student) {
            Log.d(TAG, "Error");
        }
        else {
            Log.d(TAG, "In Seminar activity: Teacher");

            setContentView(R.layout.activity_seminar_teacher);
            et_seminar_name = (EditText) findViewById(R.id.etSeminarName);
            et_seminar_name.setText(seminar_name);
            seminar_id = intent.getStringExtra(Seminar.ID);
            listSetup();

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(!is_student && (et_seminar_name.getText().toString() != seminar_name)) {
            Log.d(TAG, "Seminario editado");
            // Submete alterações ao WebService
            Map<String,String> params = new HashMap<>();
            params.put("id",seminar_id);
            params.put("name", et_seminar_name.getText().toString());
            StringRequest request = factory.POSTEditSeminar(params, SeminarActivity.this);
            VolleySingleton.getInstance(SeminarActivity.this).addToRequestQueue(request);

        }
    }

    /**
     * Callback para botão de deletar seminário
     */
    public void deleteSeminar (View view) {

        if (this.students.get(0).getId() == view.getContext().getString(R.string.no_students_enrolled)) {

            final AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle(R.string.deleteSeminar);

            final TextView text = new TextView(this);
            text.setText(R.string.deleteSeminarExplanation);
            alert.setView(text);

            alert.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    Map<String, String> params = new HashMap<>();
                    params.put("id", seminar_id);
                    StringRequest request = factory.POSTDeleteSeminar(params, SeminarActivity.this);
                    VolleySingleton.getInstance(SeminarActivity.this).addToRequestQueue(request);

                    startActivity(new Intent(SeminarActivity.this, ProfileActivity.class));

                    finish();
                }
            });

            alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Cancelado
                }
            });

            alert.show();
        } else {
            Context context = view.getContext();
            Toast.makeText(context,context.getString(R.string.seminar_delete_fail),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Faz a requisição da lista de usuários matriculados naquele seminário
     */
    private void listSetup () {
        students_list = (ListView) findViewById(R.id.lvStudents);
        students = new ArrayList<>();

        adapter = new ArrayAdapter(this, R.layout.student_row, R.id.tvStudent, this.students);
        students_list.setAdapter(adapter);

        Toast.makeText(SeminarActivity.this, R.string.students_retrieval, Toast.LENGTH_SHORT).show();
        final Map<String,String> params = new HashMap<>();
        params.put("seminar_id", seminar_id);
        StringRequest POSTRequest = factory.POSTStudentsEnrolled(this, students, adapter,params);
        VolleySingleton.getInstance(SeminarActivity.this).addToRequestQueue(POSTRequest);
    }

    /**
     * Exibe o QRCode para ser scanned pelos alunos para confirmarem
     * presença naquele determinado seminário
     */
    public void showQR(View view) {
        final Button bnt = (Button) findViewById(R.id.bntGenQR);
        bnt.setEnabled(false);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.qrcode);

        final ImageView imageView = new ImageView(this);
        if (bitmap == null)
            bitmap = generateQR(seminar_id);
        imageView.setImageBitmap(bitmap);
        alert.setView(imageView);

        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelado
                bnt.setEnabled(true);
            }
        });
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                bnt.setEnabled(true);
            }
        });

        alert.show();

    }

    /**
     * Gera o QRCode
     */
    public Bitmap generateQR (String s) {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        try {
            Bitmap bitmap = QRCodeManager.generate(s, width);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Método de confirmação no qual o professor faz um scan do
     * código de barras da carteirinha do aluno e confirma a presença
     * do mesmo no seminário
     */
    public void scanBarcode(View v) {
        try {
            Intent intent = new Intent(QRCodeManager.ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "PRODUCT_CODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(this, R.string.no_scanner, Toast.LENGTH_LONG);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String content = intent.getStringExtra("SCAN_RESULT");


                VolleySingleton.getInstance(SeminarActivity.this).addToRequestQueue(
                        factory.ConfirmStudent (SeminarActivity.this, content.substring(1), seminar_id, students, adapter)
                );
            }
        }
    }


}
