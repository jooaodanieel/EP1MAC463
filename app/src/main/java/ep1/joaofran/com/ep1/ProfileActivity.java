package ep1.joaofran.com.ep1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import lib.Seminar;
import lib.User;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "PROFILE";
    private ListView seminars_list;
    private ArrayList<Seminar> seminars;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        seminars = new ArrayList<Seminar>();
        seminars.add(new Seminar(1, "a"));
        seminars.add(new Seminar(2, "b"));
        seminars.add(new Seminar(3, "c"));
        seminars.add(new Seminar(4, "d"));


        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final String extra = intent.getStringExtra(Intent.EXTRA_TEXT);

        User user = new User("EU", "1234567", "123", extra.charAt(7) == 'S');

        if (user.isStudent()) {
            setContentView(R.layout.activity_student);
            this.seminars_list = (ListView) findViewById(R.id.lvStudentSeminar);
        }
        else {
            setContentView(R.layout.activity_teacher);
            this.seminars_list = (ListView) findViewById(R.id.lvTeacherSeminar);
        }



        adapter = new ArrayAdapter<Seminar>(this, R.layout.row, R.id.tvSeminar, this.seminars);
        seminars_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent item_click = new Intent(ProfileActivity.this, SeminarActivity.class);
                item_click.putExtra(Intent.EXTRA_TEXT, extra);
                startActivity(item_click);
            }

        });

        this.seminars_list.setAdapter(adapter);
        Log.d (TAG, "In profile activity");
    }
}
