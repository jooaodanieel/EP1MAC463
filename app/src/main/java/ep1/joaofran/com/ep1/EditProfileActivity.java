package ep1.joaofran.com.ep1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import lib.RequestFactory;
import lib.User;
import lib.VolleySingleton;

public class EditProfileActivity extends AppCompatActivity {

    private final String TAG = "EditProfileActivity";

    private EditText et_name;
    private TextView tv_nusp;
    private EditText et_pass;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefs_editor;
    private RequestFactory factory = new RequestFactory();

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    public boolean onPrepareOptionsMenu(Menu menu){
        menu.findItem(R.id.refresh).setVisible(false);
        menu.findItem(R.id.edit).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.itLogOut):
                // limpa sessão
                prefs_editor.clear();
                prefs_editor.commit();
                // volta para login sem histórico de activities
                Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            case (android.R.id.home):
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setTitle(R.string.editprofile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Log.d(TAG, "In Edit Profile");

        prefs = getSharedPreferences(getString(R.string.shared_preferences_file),MODE_PRIVATE);
        prefs_editor = prefs.edit();

        tv_nusp = (TextView) findViewById(R.id.tvEditNUSP);
        et_name = (EditText) findViewById(R.id.etEditName);
        et_pass = (EditText) findViewById(R.id.etEditPassword);

        tv_nusp.setText(prefs.getString(User.ID, "default"));
    }

    public void edit(View view) {
        String name = et_name.getText().toString();
        String pass = et_pass.getText().toString();

        if (!name.isEmpty() && !pass.isEmpty()) {
            // submete alterações para WebService
            String nusp = prefs.getString(User.ID,"0000000");
            Boolean is_student = prefs.getBoolean(User.TYPE, true);
            VolleySingleton.getInstance(view.getContext()).addToRequestQueue(
                    factory.POSTEditUser(view.getContext(), nusp, is_student, name,pass));
        } else {
            // não submete nada ao WebService e limpa os campos
            Toast.makeText(view.getContext(), getString(R.string.incorrect_info),Toast.LENGTH_SHORT).show();
            et_name.setText("");
            et_pass.setText("");
        }
    }
}
