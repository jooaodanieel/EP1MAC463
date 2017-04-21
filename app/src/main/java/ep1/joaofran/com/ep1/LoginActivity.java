package ep1.joaofran.com.ep1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText login;
    private EditText senha;
    private static final String TAG = "LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (EditText) findViewById(R.id.etNUSP);
        senha = (EditText) findViewById(R.id.etPassword);
    }

    public void onClick (View view) {
        String l = login.getText().toString();
        String p = senha.getText().toString();

        Log.d(TAG, "Botão clicked");

        LoginTask task = new LoginTask("login","senha");
        task.execute("string qualquer");
    }
}
