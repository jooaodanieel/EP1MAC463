package ep1.joaofran.com.ep1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "PROFILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Log.d (TAG, "In profile activity");



        // alguma coisinha
    }
}
