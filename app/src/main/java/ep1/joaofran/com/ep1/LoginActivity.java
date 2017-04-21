package ep1.joaofran.com.ep1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import tasks.GetTask;
import tasks.LoginTask;

public class LoginActivity extends AppCompatActivity {

    private EditText login;
    private EditText password;
    private static final String TAG = "LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (EditText) findViewById(R.id.etNUSP);
        password = (EditText) findViewById(R.id.etPassword);
    }

    public void onClick (View view) {
        String l = login.getText().toString();
        String p = password.getText().toString();

        Log.d(TAG, "Botão clicked");

        new GetTask().execute("teacher");
    }
}
