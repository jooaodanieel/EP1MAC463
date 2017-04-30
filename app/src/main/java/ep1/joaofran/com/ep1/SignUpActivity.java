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

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    private EditText et_name;
    private EditText et_login;
    private EditText et_password;
    private RadioGroup type;

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefs_editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle(R.string.signup_title);

        Log.d (TAG, "In SignUp activity");

        et_name = (EditText) findViewById(R.id.etInsertName);
        et_login = (EditText) findViewById(R.id.etInsertNUSP);
        et_password = (EditText) findViewById(R.id.etPassword);

        prefs = getSharedPreferences(getString(R.string.shared_preferences_file), MODE_PRIVATE);
        prefs_editor = prefs.edit();

    }

    public void signUp(View view) {

        // Sign up no webserver
        //getCheckedRadioButtonId()

        if () {

        } else {

        }

        Log.d (TAG, "Sign Up successfull");

        Intent intent = new Intent(view.getContext(), ProfileActivity.class);
        //Mandar informações do usuário
        startActivity(intent);
    }

    public void linkLogin(View view) {
        Log.d(TAG, "To Login activity");

        Intent intent = new Intent(view.getContext(), LoginActivity.class);
        startActivity(intent);
    }
}
