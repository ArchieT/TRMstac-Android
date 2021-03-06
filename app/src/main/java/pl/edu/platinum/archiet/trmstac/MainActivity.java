package pl.edu.platinum.archiet.trmstac;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.IntUnaryOperator;

public class MainActivity extends AppCompatActivity {

    private ListView naszalista;
    private Button refreshbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        naszalista = (ListView) findViewById(R.id.listView);
        refreshbutton = (Button) findViewById(R.id.button);
        refreshbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshClickHandler(view);
            }
        });
    }

    public void refreshClickHandler(View view) {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(MainActivity.this, "Startujem", Toast.LENGTH_SHORT).show();
            new DownloadTRMTask().execute();
        } else {
            Toast.makeText(MainActivity.this, "Brak połączenia sieciowego!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadTRM() throws IOException, GoException {
        InputStream is = null;
        try {
            URL url = new URL(Mobile.THE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("pobieranie", "The response is: " + response);
            is = conn.getInputStream();
            Mobile.TakeWewnString(readIt(is, 3000000));
            String goerr = Mobile.ParseAll();
            if (goerr.length() > 0) {
                throw new GoException(goerr);
            }
            goerr = Mobile.ZipUzS();
            if (goerr.length() > 0) {
                throw new GoException(goerr);
            }
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private class DownloadTRMTask extends AsyncTask<Void, Void, Void> {
        //@Override
        protected void doInBackground() {
            try {
                downloadTRM();
            } catch (IOException e) {
                final IOException oe = e;
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Unable to download" + oe.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (GoException e) {
                final GoException oe = e;
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "GoException" + oe.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        //@Override
        protected void onPostExecute() {
            ListViewAdapter adapter =new ListViewAdapter(MainActivity.this);
            naszalista.setAdapter(adapter);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            this.doInBackground();
            return null;
        }
    }
    public String readIt(InputStream stream, int len) throws IOException {
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder writer = new StringBuilder();
        String line;
        while ((line =reader.readLine())!=null) {
            writer.append(line);
        }
        String str = writer.toString();
        return str;
    }
}

