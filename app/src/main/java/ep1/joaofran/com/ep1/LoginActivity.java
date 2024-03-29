package ep1.joaofran.com.ep1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import lib.RequestFactory;
import lib.User;
import lib.VolleySingleton;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText et_login;
    private EditText et_password;
    private RadioGroup radioGroup;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefs_editor;
    private RequestFactory factory = new RequestFactory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle(R.string.login_title);

        Log.d (TAG, "In Login activity");

        prefs = getSharedPreferences(getString(R.string.shared_preferences_file),MODE_PRIVATE);
        prefs_editor = prefs.edit();

        if (prefs.contains(User.ID)) {
            // se tiver sessão ativa, já muda direto de activity
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            et_login = (EditText) findViewById(R.id.etNUSP);
            et_password = (EditText) findViewById(R.id.etPassword);
            radioGroup = (RadioGroup) findViewById(R.id.rgLoginType);
        }
    }

    public void login (View view) {
        if (!et_login.getText().toString().isEmpty() &&
                !et_password.getText().toString().isEmpty() &&
                radioGroup.getCheckedRadioButtonId() != -1) {

            // submete informações ao WebService
            String login = et_login.getText().toString();
            String password = et_password.getText().toString();
            RadioButton checked = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
            Boolean u_type_student = checked.getText().toString() == getString(R.string.student);

            Log.d(TAG, "Button clicked");

            VolleySingleton.getInstance(view.getContext())
                    .addToRequestQueue(factory.POSTLogin(view.getContext(), login, password, u_type_student, prefs_editor));
        } else {
            Toast.makeText(view.getContext(),getString(R.string.incorrect_info),Toast.LENGTH_LONG).show();
        }

    }

    public void linkSignUp (View view) {
        Log.d(TAG, "To Sign Up activity");

        Intent intent = new Intent(view.getContext(), SignUpActivity.class);
        startActivity(intent);
    }

}
