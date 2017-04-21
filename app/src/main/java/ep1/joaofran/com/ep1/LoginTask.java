package ep1.joaofran.com.ep1;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by joaofran on 21/04/17.
 */

public class LoginTask extends AsyncTask<String,Void,Boolean> {

    private String login, senha;
    private static final String TAG = "LOGINTASK";

    public LoginTask (String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Log.d(TAG,"inBackground");

        try {
            String query = String.format("nusp=%s&pass=%s",
                    "6798294","e10adc3949ba59abbe56e057f20f883e");
                    /*URLEncoder.encode(this.login,"UTF-8"),
                    URLEncoder.encode(this.senha,"UTF-8"))*/;
            URL url = new URL("http://207.38.82.139:8001/login/student?"+query);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String s;
            while ((s = br.readLine()) != null) {
                Log.d(TAG,s);
            }

            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
