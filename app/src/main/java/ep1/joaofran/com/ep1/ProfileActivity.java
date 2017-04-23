package ep1.joaofran.com.ep1;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.content.DialogInterface;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import lib.Seminar;
import lib.User;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private ListView seminars_list;
    private ArrayList<Seminar> seminars;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Exemple
        seminars = new ArrayList<Seminar>();
        seminars.add(new Seminar(1, "a"));
        seminars.add(new Seminar(2, "b"));
        seminars.add(new Seminar(3, "c"));
        seminars.add(new Seminar(4, "d"));


        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        String u_num = intent.getStringExtra(User.ID);
        Boolean u_student = intent.getBooleanExtra(User.TYPE, true);


        if (u_student) {
            Log.d(TAG, "In Profile activity: Student");

            setContentView(R.layout.activity_student);
            this.seminars_list = (ListView) findViewById(R.id.lvStudentSeminar);
        }
        else {
            Log.d(TAG, "In Profile activity: Teacher");

            setContentView(R.layout.activity_teacher);
            this.seminars_list = (ListView) findViewById(R.id.lvTeacherSeminar);
        }

        listSetup(u_num, u_student);
    }

    private void listSetup(String num, Boolean student) {
        Log.d(TAG, "Setting up seminar list");

        final String u_num = num;
        final Boolean u_student = student;

        // GET todos seminarios


        adapter = new ArrayAdapter<Seminar>(this, R.layout.row, R.id.tvSeminar, this.seminars);
        seminars_list.setAdapter(adapter);

        seminars_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent item_click = new Intent(ProfileActivity.this, SeminarActivity.class);
                item_click.putExtra(User.ID, u_num);
                item_click.putExtra(User.TYPE, u_student);
                item_click.putExtra(Seminar.ID, seminars.get(position).getName());
                startActivity(item_click);
            }

        });
    }

    public void addSeminar(View view) {
        Log.d(TAG, "Adding new seminar");

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.newSeminar);

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // POST seminar
                // GET seminar

                Seminar sem = new Seminar(19, input.getText().toString());
                seminars.add(0, sem);
            }
        });

        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelado
            }
        });

        alert.show();
    }
}
