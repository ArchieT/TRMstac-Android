package pl.edu.platinum.archiet.trmstac;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;

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
                refresh();
            }
        });
    }

    void refresh() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Mobile.GoHTTPDownloadString();
            String err = Mobile.ParseAll();
            if (err != null) {
                Toast.makeText(MainActivity.this, err, Toast.LENGTH_LONG);
            } else {
                String errn = Mobile.ZipUzS();
                if (err != null) {
                    Toast.makeText(MainActivity.this, errn, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "mamy!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(MainActivity.this, "Brak połączenia sieciowego!!", Toast.LENGTH_SHORT).show();
        }
    }
}
