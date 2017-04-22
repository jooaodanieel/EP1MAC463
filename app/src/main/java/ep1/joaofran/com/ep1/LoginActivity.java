package ep1.joaofran.com.ep1;

import android.content.Intent;
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
//        setContentView(R.layout.activity_teacher);
    }

    public void login (View view) {
        String l = login.getText().toString();
        String p = password.getText().toString();

        Log.d(TAG, "Button clicked");

        // Fazer login no webservice new GetTask().execute("teacher");
        if (true) {
            //Succsess
            Intent intent = new Intent (view.getContext(), ProfileActivity.class);
            // Mandar de alguma forma informação se é aluno ou professor, Jason mais uma variavel de identificação?
            startActivity(intent);
        }
        else {
            // Faliure
            // Mensagem de erro, Toast??
        }
    }

    public void linkSignUp (View view) {
        Intent intent = new Intent(view.getContext(), SignUpActivity.class);
        startActivity(intent);
    }

}
