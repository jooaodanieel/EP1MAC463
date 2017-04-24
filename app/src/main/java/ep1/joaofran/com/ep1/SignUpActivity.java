package ep1.joaofran.com.ep1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    private EditText name;
    private EditText login;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle(R.string.signup_title);

        Log.d (TAG, "In SignUp activity");
    }

    public void signUp(View view) {

        // Sign up no webserver

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
