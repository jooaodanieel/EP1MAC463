package ep1.joaofran.com.ep1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import lib.RequestFactory;
import lib.User;

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

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.itLogOut):
                // clear no shared preferences
                prefs_editor.clear();
                prefs_editor.commit();
                Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case (R.id.home):
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setTitle(R.string.editprofile);

        prefs = getSharedPreferences(getString(R.string.shared_preferences_file),MODE_PRIVATE);
        prefs_editor = prefs.edit();

        tv_nusp = (TextView) findViewById(R.id.tvEditNUSP);
        et_name = (EditText) findViewById(R.id.etEditName);
        et_pass = (EditText) findViewById(R.id.etEditPassword);


    }
}
