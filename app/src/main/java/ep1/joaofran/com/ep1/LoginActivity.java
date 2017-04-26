package ep1.joaofran.com.ep1;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import lib.User;
import tasks.GetTask;
import tasks.LoginTask;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText et_login;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle(R.string.login_title);


        Log.d (TAG, "In Login activity");

        et_login = (EditText) findViewById(R.id.etNUSP);
        et_password = (EditText) findViewById(R.id.etPassword);
    }

    public void login (View view) {
        String login = et_login.getText().toString();
        String password = et_password.getText().toString();

        Log.d(TAG, "Button clicked");

        // Login no webbserver

        if (true) {
            Log.d(TAG, "Log In successful");

            Intent intent = new Intent (view.getContext(), ProfileActivity.class);
            intent.putExtra(User.ID, login);
            intent.putExtra(User.TYPE, false);
            startActivity(intent);
        }
        else {
            Log.d(TAG, "Log In failed");

            Toast.makeText(view.getContext(), R.string.login_fail, Toast.LENGTH_LONG).show();
        }
    }

    public void linkSignUp (View view) {
        Log.d(TAG, "To Sign Up activity");

        Intent intent = new Intent(view.getContext(), SignUpActivity.class);
        startActivity(intent);
    }

}
