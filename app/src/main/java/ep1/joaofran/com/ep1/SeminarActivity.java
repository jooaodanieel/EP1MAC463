package ep1.joaofran.com.ep1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class SeminarActivity extends AppCompatActivity {

    private EditText seminar_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        String extra = intent.getStringExtra(Intent.EXTRA_TEXT);

        if (extra.charAt(7) == 'S')
            setContentView(R.layout.activity_seminar_student);
        else
            setContentView(R.layout.activity_seminar_teacher);

        seminar_name = (EditText) findViewById(R.id.etSeminarName);
        seminar_name.setText("Palestra do Zé");
    }
}
