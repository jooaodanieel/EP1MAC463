package ep1.joaofran.com.ep1;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import lib.QRCodeManager;
import lib.Seminar;
import lib.User;

public class SeminarActivity extends AppCompatActivity {

    private static final String TAG = "SeminarActivity";

    private EditText et_seminar_name;
    private TextView tv_seminar_name;
    private String seminar_id;
    private Bitmap bitmap = null;

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
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ActionBar setup
        setTitle(R.string.seminar_title);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();

        if (intent.getBooleanExtra(User.TYPE, true)) {
            Log.d(TAG, "In Seminar activity: Student");

            setContentView(R.layout.activity_seminar_student);
            tv_seminar_name = (TextView) findViewById(R.id.tvSeminarName);
            tv_seminar_name.setText(intent.getStringExtra(Seminar.ID));
        }
        else {
            Log.d(TAG, "In Seminar activity: Teacher");

            setContentView(R.layout.activity_seminar_teacher);
            et_seminar_name = (EditText) findViewById(R.id.etSeminarName);
            et_seminar_name.setText(intent.getStringExtra(Seminar.NAME));
            seminar_id = intent.getStringExtra(Seminar.ID);
        }

    }

    public void showQR(View view) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.qrcode);

        final ImageView imageView = new ImageView(this);
        if (bitmap == null)
            bitmap = generateQR(seminar_id);
        imageView.setImageBitmap(bitmap);
        alert.setView(imageView);

        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelado
            }
        });

        alert.show();
    }


    public Bitmap generateQR (String s) {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        try {
            Bitmap bitmap = QRCodeManager.generate(s, width);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void scanQR(View v) {
        try {
            Intent intent = new Intent(QRCodeManager.ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            Toast toast = Toast.makeText(this, "No Scanner found", Toast.LENGTH_LONG);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");

                // Confirmar no webserver
                Toast toast = Toast.makeText(this, "Content:" + contents, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    // On destroy ou no stop verificar se precisa editar o Seminário
}
