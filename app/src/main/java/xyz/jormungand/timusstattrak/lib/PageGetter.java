package xyz.jormungand.timusstattrak.lib;

import android.os.AsyncTask;
import android.os.StrictMode;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;

public class PageGetter extends AsyncTask<String,Void,String> {

    private static String string;
    private URL url;

    public String getPage(URL url) throws IOException {
        this.url = url;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return doInBackground();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            string = "";
            LineNumberReader r = new LineNumberReader(new InputStreamReader(url.openStream()));
            String s = r.readLine();
            while (s != null) {
                string+=(s);
                s = r.readLine();
            }
            r.close();
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
        return string;
    }
}


