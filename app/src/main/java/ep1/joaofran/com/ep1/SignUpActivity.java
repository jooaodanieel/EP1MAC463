package ep1.joaofran.com.ep1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import lib.RequestFactory;
import lib.User;
import lib.VolleySingleton;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    private EditText et_name;
    private EditText et_login;
    private EditText et_password;
    private RadioGroup rg_type;

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefs_editor;

    private RequestFactory factory = new RequestFactory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle(R.string.signup_title);

        Log.d (TAG, "In SignUp activity");

        et_name = (EditText) findViewById(R.id.etInsertName);
        et_login = (EditText) findViewById(R.id.etInsertNUSP);
        et_password = (EditText) findViewById(R.id.etInsertPassword);
        rg_type = (RadioGroup) findViewById(R.id.rgSignupType);

        prefs = getSharedPreferences(getString(R.string.shared_preferences_file), MODE_PRIVATE);
        prefs_editor = prefs.edit();

    }

    public void signUp(View view) {

        Log.d(TAG, "button clicked");
        // Sign up no webserver
        String name = et_name.getText().toString();
        String login = et_login.getText().toString();
        String password = et_password.getText().toString();
        Boolean is_student = (rg_type.getCheckedRadioButtonId() == R.id.rbStudent);


        if (name.isEmpty() || login.isEmpty() || password.isEmpty()) {
            Toast.makeText(view.getContext(), R.string.incorrect_info, Toast.LENGTH_LONG).show();
        } else {
            VolleySingleton.getInstance(view.getContext())
                    .addToRequestQueue(factory.POSTSignUp(view.getContext(), name, login, password, is_student, prefs_editor));
        }

    }

    public void linkLogin(View view) {
        Log.d(TAG, "To Login activity");

       finish();
    }
}
