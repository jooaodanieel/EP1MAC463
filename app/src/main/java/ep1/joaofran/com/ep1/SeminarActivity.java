package ep1.joaofran.com.ep1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import lib.Seminar;
import lib.User;

public class SeminarActivity extends AppCompatActivity {

    private static final String TAG = "SeminarActivity";

    private EditText et_seminar_name;
    private TextView tv_seminar_name;

    // Action Bar
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.itLogOut):
                Intent intent = new Intent(SeminarActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            case (R.id.itBack):
                this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(R.string.seminar_title);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if (intent.getBooleanExtra(User.TYPE, true)) {
            Log.d(TAG, "In Seminar activity: Student");

            setContentView(R.layout.activity_seminar_student);
            tv_seminar_name = (TextView) findViewById(R.id.tvSeminarName);
            tv_seminar_name.setText(intent.getStringExtra(Seminar.ID));
        }
        else {
            Log.d(TAG, "In Seminar activity: Student");

            setContentView(R.layout.activity_seminar_teacher);
            et_seminar_name = (EditText) findViewById(R.id.etSeminarName);
            et_seminar_name.setText(intent.getStringExtra(Seminar.ID));
        }

    }

    // On destroy ou no stop verificar se precisa editar o Seminário
}
