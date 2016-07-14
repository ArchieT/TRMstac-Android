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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
            Mobile.TakeWewnString(readIt(is, 30000));
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
        @Override
        protected String doInBackground() {
            try {
                downloadTRM();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "Unable to download" + e.toString(), Toast.LENGTH_SHORT).show();
            } catch (GoException e) {
                Toast.makeText(MainActivity.this, "GoException" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecute() {
            ListViewAdapter adapter =new ListViewAdapter(MainActivity.this);
            naszalista.setAdapter(adapter);
        }
    }
    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}

