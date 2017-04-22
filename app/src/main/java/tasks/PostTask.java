package tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PostTask extends AsyncTask<String, Void, Boolean> {

    private static final String URL_BEGIN = "http://207.38.82.139:8001/";
    private static final String TAG = "POSTTASK";

    private String url;
    private String[] params;

    public PostTask (String... params) {
        this.params = params;
    }


    @Override
    protected Boolean doInBackground (String... url_end){

        try {
            this.url = URL_BEGIN + url_end[0];
            Log.d(TAG, "in Background " + this.url);
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(this.url).openConnection();

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

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
